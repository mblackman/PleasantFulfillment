package app.mblackman.orderfulfillment.data.network.etsy

import android.content.Context
import app.mblackman.orderfulfillment.data.network.*
import app.mblackman.orderfulfillment.data.network.etsy.json.EtsyResponseWrapper
import app.mblackman.orderfulfillment.data.network.etsy.json.Listing
import retrofit2.Response
import timber.log.Timber

class EtsyStoreAdapter(
    private val etsyApiService: EtsyApiService,
    private val configuration: Configuration
) : StoreAdapter {

    companion object {
        fun create(context: Context): EtsyStoreAdapter {
            val sessionManager = SessionManager(context)
            val configuration = SharedPreferencesConfiguration(context)
            val etsyApiService =
                EtsyServiceGenerator(sessionManager).createService(EtsyApiService::class.java)
            return EtsyStoreAdapter(etsyApiService, configuration)
        }
    }

    override val adapterId: Int = 1

    override suspend fun getOrders(): List<NetworkOrder> {
        return safeApiCall(error = "Failed to get receipts.") { limit, offset ->
            etsyApiService.findAllReceiptsAsync(getShopId(), limit, offset).await()
        }?.map { receipt ->
            receipt.toNetworkOrder()
        } ?: throw NetworkException("Could not load receipts from Etsy.")
    }

    override suspend fun getProducts(): List<NetworkProduct> {
        return getAllListings()?.map {
            val image = safeApiCall("Couldn't get image for listing with id: ${it.id}") { _, _ ->
                etsyApiService.getImageListingAsync(it.id, 0).await()
            }
            it.toNetworkProduct(image?.first()?.imageUrl75X75)
        } ?: throw NetworkException("Couldn't load products from Etsy.")
    }

    override suspend fun getProductSales(): List<NetworkProductSale> {
        return safeApiCall("Failed to get transactions.") { limit, offset ->
            etsyApiService.findAllTransactionsAsync(getShopId(), limit, offset).await()
        }?.map {
            it.toNetworkProductSale()
        } ?: throw NetworkException("Couldn't load transactions from Etsy.")
    }

    private suspend fun getAllListings(): List<Listing>? {
        return safeApiCall("Failed to get active listings.") { limit, offset ->
            etsyApiService.findAllActiveShopListingsAsync(
                getShopId(),
                limit,
                offset
            ).await()
        }
    }

    private suspend fun getShopId(): Int = with(configuration) {
        if (this.currentUserShopId == null) {
            val shop = safeApiCall(error = "Failed to get shop") { _, _ ->
                etsyApiService.getShopSelfAsync().await()
            }

            if (shop == null || shop.isEmpty()) {
                throw NetworkException("Failed to get shop from Etsy.")
            }
            this.currentUserShopId = shop[0].id
        }
        this.currentUserId!!
    }

    /**
     * Safely make Retrofit API calls, and wrap any errors with a provided message.
     *
     * @param error The error string to provide on error.
     * @param pageSize The number of items to get per API response page.
     * @param call The Retrofit API call to make.
     *
     * @return The value retrieved from the API.
     */
    private suspend fun <T : Any> safeApiCall(
        error: String,
        pageSize: Int = 50,
        call: suspend (limit: Int, offset: Int) -> Response<EtsyResponseWrapper<T>>
    ): List<T>? {
        return try {
            var currentOffset = 0
            var isValid = true
            val returnValues = mutableListOf<T>()

            while (isValid) {
                val response = call(pageSize, currentOffset)
                Timber.i(response.toString())

                if (response.isSuccessful) {

                    returnValues.addAll(response.body()!!.results)

                    if (response.body()!!.count < pageSize) {
                        // The result contained fewer results than expected, which means no
                        // more items remain.
                        isValid = false
                    } else {
                        // Move to the next page.
                        currentOffset += pageSize
                    }
                } else {
                    Timber.e("Oops .. Something went wrong due to  $error")
                    return null
                }
            }

            return returnValues

        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }
}
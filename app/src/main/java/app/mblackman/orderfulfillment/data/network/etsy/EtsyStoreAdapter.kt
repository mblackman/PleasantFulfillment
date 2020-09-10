package app.mblackman.orderfulfillment.data.network.etsy

import app.mblackman.orderfulfillment.data.common.Failure
import app.mblackman.orderfulfillment.data.common.Result
import app.mblackman.orderfulfillment.data.common.Success
import app.mblackman.orderfulfillment.data.network.*
import app.mblackman.orderfulfillment.data.network.etsy.json.EtsyResponseWrapper
import app.mblackman.orderfulfillment.data.network.etsy.json.Receipt
import app.mblackman.orderfulfillment.data.network.etsy.json.Transaction
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

/**
 * The adapter for an Etsy Store.
 */
class EtsyStoreAdapter @Inject constructor(
    private val etsyApiService: EtsyApiService,
    private val configuration: Configuration
) : StoreAdapter {

    override val adapterId: Int = 1
    private val pageSize: Int = 50

    override suspend fun getOrders(): Result<List<NetworkOrder>> {
        val shopId =
            getShopId() ?: return Failure(NetworkException("Failed to get the user's shop id."))
        return safeApiCall(
            error = "Failed to get receipts",
            call = { limit, offset ->
                etsyApiService.findAllReceiptsAsync(shopId, limit, offset).await()
            },
            convert = Receipt::toNetworkOrder
        )
    }

    override suspend fun getProducts(): Result<List<NetworkProduct>> {
        val shopId =
            getShopId() ?: return Failure(NetworkException("Failed to get the user's shop id."))
        return safeApiCall(
            "Failed to get active listings",
            call = { limit, offset ->
                etsyApiService.findAllActiveShopListingsAsync(
                    shopId,
                    limit,
                    offset
                ).await()
            },
            convert = { product -> product.toNetworkProduct(getProductImageUrl(product.id)) }
        )
    }

    override suspend fun getProductSales(): Result<List<NetworkProductSale>> {
        val shopId =
            getShopId() ?: return Failure(NetworkException("Failed to get the user's shop id."))
        return safeApiCall(
            error = "Failed to get transactions",
            call = { limit, offset ->
                etsyApiService.findAllTransactionsAsync(shopId, limit, offset).await()
            },
            convert = Transaction::toNetworkProductSale
        )
    }

    private suspend fun getProductImageUrl(productId: Int): String? {
        val result = safeApiCall(
            error = "Couldn't get image for listing with id: $productId",
            call = { _, _ -> etsyApiService.getImageListingAsync(productId, 0).await() },
            convert = { image -> image.imageUrl75X75 }
        )

        return when (result) {
            is Success -> result.result.firstOrNull()
            is Failure -> null
        }
    }

    private suspend fun getShopId(): Int? =
        with(configuration) {
            if (this.currentUserShopId == null) {
                val result = safeApiCall(
                    error = "Failed to get shop",
                    call = { _, _ -> etsyApiService.getShopSelfAsync().await() },
                    convert = { shop -> shop.id }
                )

                if (result is Success) {
                    this.currentUserShopId = result.result.firstOrNull()
                }
            }

            this.currentUserShopId
        }

    /**
     * Safely make Retrofit API calls, and wrap any errors with a provided message.
     *
     * @param error The error string to provide on error.
     * @param call The Retrofit API call to make.
     *
     * @return The value retrieved from the API.
     */
    private suspend fun <T : Any, V : Any> safeApiCall(
        error: String,
        call: suspend (limit: Int, offset: Int) -> Response<EtsyResponseWrapper<T>>,
        convert: suspend (input: T) -> V
    ): Result<List<V>> {
        try {
            var currentOffset = 0
            var isValid = true
            val returnValues = mutableListOf<V>()

            while (isValid) {
                val response = call(pageSize, currentOffset)
                Timber.i(response.toString())

                if (response.isSuccessful) {
                    val body = response.body()
                        ?: return Failure(NetworkException("Etsy API response body was empty."))

                    returnValues.addAll(body.results.map { convert(it) })

                    if (body.count < pageSize) {
                        // The result contained fewer results than expected, which means no
                        // more items remain.
                        isValid = false
                    } else {
                        // Move to the next page.
                        currentOffset += pageSize
                    }
                } else {
                    Timber.e(
                        "Etsy adapter failed to load API response: ${
                            response.errorBody()?.string()
                        }"
                    )
                    return Failure(NetworkException(error))
                }
            }

            return Success(returnValues)

        } catch (e: Exception) {
            return Failure(e)
        }
    }
}
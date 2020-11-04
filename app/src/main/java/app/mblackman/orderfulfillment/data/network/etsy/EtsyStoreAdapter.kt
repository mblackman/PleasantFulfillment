package app.mblackman.orderfulfillment.data.network.etsy

import app.mblackman.orderfulfillment.data.network.NetworkOrder
import app.mblackman.orderfulfillment.data.network.NetworkProduct
import app.mblackman.orderfulfillment.data.network.NetworkProductSale
import app.mblackman.orderfulfillment.data.network.StoreAdapter
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
    private val etsyApiService: EtsyApiService
) : StoreAdapter {
    private val pageSize: Int = 50
    private var _hasValidSession: Boolean = false
    private var shopId: Int? = null

    override val adapterId: Int = 1

    override val hasValidSession: Boolean
        get() = _hasValidSession

    /**
     * Initializes the [StoreAdapter] to start any services and get important information.
     */
    override suspend fun initialize() {
        val request = safeApiCall(
            call = { _, _ -> etsyApiService.getShopSelfAsync().await() },
            convert = { shop -> shop.id }
        )?.firstOrNull()

        if (request != null) {
            shopId = request
            _hasValidSession = true
        } else {
            shopId = null
            _hasValidSession = false
        }
    }

    override suspend fun getOrders(): List<NetworkOrder>? {
        val shopId = validateAndGetShopId() ?: return null

        return safeApiCall(
            call = { limit, offset ->
                etsyApiService.findAllReceiptsAsync(shopId, limit, offset).await()
            },
            convert = Receipt::toNetworkOrder
        )
    }

    override suspend fun getProducts(): List<NetworkProduct>? {
        val shopId = validateAndGetShopId() ?: return null

        return safeApiCall(
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

    override suspend fun getProductSales(): List<NetworkProductSale>? {
        val shopId = validateAndGetShopId() ?: return null

        return safeApiCall(
            call = { limit, offset ->
                etsyApiService.findAllTransactionsAsync(shopId, limit, offset).await()
            },
            convert = Transaction::toNetworkProductSale
        )
    }

    private suspend fun getProductImageUrl(productId: Int): String? =
        safeApiCall(
            call = { _, _ -> etsyApiService.getImageListingAsync(productId, 0).await() },
            convert = { image -> image.imageUrl75X75 }
        )?.firstOrNull()

    private suspend fun validate() {
        if (!hasValidSession || shopId == null) {
            initialize()
        }
    }

    private suspend fun validateAndGetShopId(): Int? {
        validate()
        if (hasValidSession && shopId != null) {
            return shopId
        }
        return null
    }

    private suspend fun <T : Any, V : Any> safeApiCall(
        call: suspend (limit: Int, offset: Int) -> Response<EtsyResponseWrapper<T>>,
        convert: suspend (input: T) -> V
    ): List<V>? {
        try {
            var currentOffset = 0
            var isValid = true
            val returnValues = mutableListOf<V>()

            while (isValid) {
                val response = try {
                    call(pageSize, currentOffset)
                } catch (e: Throwable) {
                    Timber.e(e)
                    return null
                }

                Timber.i(response.toString())

                if (response.isSuccessful) {
                    val body = response.body()
                        ?: return null

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
                        "Etsy adapter failed to load API code: ${response.code()}, error body: ${
                            response.errorBody()?.string()
                        }"
                    )

                    return null
                }
            }

            return returnValues
        } catch (e: Exception) {
            Timber.e(e)
        }

        return null
    }
}
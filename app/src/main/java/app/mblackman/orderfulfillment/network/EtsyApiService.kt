package app.mblackman.orderfulfillment.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Provides an interface for methods used on Etsy api.
 */
interface EtsyApiService {

    /**
     * Gets all unshipped orders for the given shop.
     *
     * @param shopId The id of the shop to get orders for.
     * @return A list of all receipts.
     */
    @GET("shops/{shopId}/receipts")
    fun getUnshippedReceiptsAsync(@Path("shopId") shopId: Int):
            Deferred<List<Receipt>>
}

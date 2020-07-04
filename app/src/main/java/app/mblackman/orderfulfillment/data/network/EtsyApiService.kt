package app.mblackman.orderfulfillment.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Provides an interface for methods used on Etsy api.
 */
interface EtsyApiService {

    enum class ShipmentStatus {
        OPEN, UNSHIPPED, UNPAID, COMPLETED, PROCESSING, ALL
    }

    /**
     * Gets all orders for the given shop.
     *
     * @param shopId The id of the shop to get orders for.
     * @return A list of all receipts.
     */
    @GET("shops/{shopId}/receipts/{status}")
    suspend fun getReceiptsAsync(
        @Path("shopId") shopId: Int,
        @Path("status") status: ShipmentStatus = ShipmentStatus.ALL
    ): Response<EtsyResponseWrapper<Receipt>>

    /**
     * Gets the user for the authenticated user.
     *
     * @return The user.
     */
    @GET("users/__SELF__")
    suspend fun getUserSelfAsync(): Response<EtsyResponseWrapper<User>>
}

package app.mblackman.orderfulfillment.data.network.etsy

import app.mblackman.orderfulfillment.data.network.etsy.json.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val LimitPropertyName = "limit"
const val OffsetPropertyName = "offset"

/**
 * Provides an interface for methods used on Etsy api.
 */
interface EtsyApiService {
    /**
     * Gets all orders for the given shop.
     *
     * @param shopId The id of the shop to get orders for.
     * @param limit The number of receipts to get per request.
     * @param offset The offset from 0 to get the receipts.
     * @return A list of all receipts.
     */
    @GET("shops/{shopId}/receipts")
    fun findAllReceiptsAsync(
        @Path("shopId") shopId: Int,
        @Query(LimitPropertyName) limit: Int,
        @Query(OffsetPropertyName) offset: Int
    ): Deferred<Response<EtsyResponseWrapper<Receipt>>>

    @GET("shops/{shopId}/transactions")
    fun findAllTransactionsAsync(
        @Path("shopId") shopId: Int,
        @Query(LimitPropertyName) limit: Int,
        @Query(OffsetPropertyName) offset: Int
    ): Deferred<Response<EtsyResponseWrapper<Transaction>>>

    @GET("shops/{shopId}/listings/active")
    fun findAllActiveShopListingsAsync(
        @Path("shopId") shopId: Int,
        @Query(LimitPropertyName) limit: Int,
        @Query(OffsetPropertyName) offset: Int
    ): Deferred<Response<EtsyResponseWrapper<Listing>>>

    @GET("listings/{listingId}/images/{listingImageId}")
    fun getImageListingAsync(
        @Path("listingId") listingId: Int,
        @Path("listingImageId") listingImageId: Int
    ): Deferred<Response<EtsyResponseWrapper<ListingImage>>>

    /**
     * Gets the user for the authenticated user.
     *
     * @return The user.
     */
    @GET("users/__SELF__")
    fun getUserSelfAsync(): Deferred<Response<EtsyResponseWrapper<User>>>

    /**
     * Gets the shop for the authenticated user.
     *
     * @return The shop belonging to the user.
     */
    @GET("shops/__SELF__")
    fun getShopSelfAsync(): Deferred<Response<EtsyResponseWrapper<Shop>>>
}

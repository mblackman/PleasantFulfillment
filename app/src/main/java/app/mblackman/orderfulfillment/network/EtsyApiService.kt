package app.mblackman.orderfulfillment.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface EtsyApiService {

    @GET("shops/{shopId}/receipts")
    fun getUnshippedReceiptsAsync(@Path("shopId") shopId: Int):
            Deferred<List<Receipt>>
}

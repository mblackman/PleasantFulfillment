package app.mblackman.orderfulfillment.network

import com.squareup.moshi.Json

/**
 * The body of a standard response from the Etsy server.
 */
data class Response<T>(
    @Json(name = "count") val count: Int,
    @Json(name = "results") val results: List<T>,
    @Json(name = "type") val type: String
)

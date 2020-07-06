package app.mblackman.orderfulfillment.data.network.json

import com.squareup.moshi.Json

/**
 * The body of a standard response from the Etsy server.
 */
data class EtsyResponseWrapper<T>(
    @Json(name = "count") val count: Int,
    @Json(name = "results") val results: List<T>,
    @Json(name = "type") val type: String
)

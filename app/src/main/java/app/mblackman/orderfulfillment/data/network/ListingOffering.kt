package app.mblackman.orderfulfillment.data.network

import com.squareup.moshi.Json

/**
 * Represents an offerings of a listing on Etsy.
 */
data class ListingOffering(
    @Json(name = "offering_id") val id: Int,
    @Json(name = "price") val price: Float,
    @Json(name = "quantity") val quantity: Int,
    @Json(name = "is_enabled") val isEnabled: Boolean,
    @Json(name = "is_deleted") val isDeleted: Boolean
)
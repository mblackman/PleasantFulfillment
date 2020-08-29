package app.mblackman.orderfulfillment.data.network.etsy.json

import com.squareup.moshi.Json

data class ListingImage(
    @Json(name = "listing_image_id") val imageId: Int,
    @Json(name = "hex_code") val hexCode: String,
    val red: Int,
    val green: Int,
    val blue: Int,
    val hue: Int,
    val saturation: Int,
    val brightness: Int,
    @Json(name = "is_black_and_white") val isBlackAndWhite: Boolean,
    @Json(name = "creation_tsz") val creationTime: Float,
    @Json(name = "listing_id") val listingId: Int,
    val rank: Int,
    @Json(name = "url_75x75") val imageUrl75X75: String,
    @Json(name = "url_170x135") val imageUrl170X135: String,
    @Json(name = "url_570xN") val imageUrl570XN: String,
    @Json(name = "url_fullxfull") val imageUrlFullSize: String,
    @Json(name = "full_height") val fullHeight: Int,
    @Json(name = "full_width") val fullWidth: Int
)
package app.mblackman.orderfulfillment.data.network.json

import com.squareup.moshi.Json

/**
 * Represents the structured property data from Etsy.
 */
data class PropertyValue(
    @Json(name = "property_id") val id: Int,
    @Json(name = "property_name") val name: String,
    @Json(name = "scale_id") val scaleId: Int,
    @Json(name = "scale_name") val scaleName: String,
    @Json(name = "value_ids") val valueIds: List<Int>,
    @Json(name = "values") val values: List<String>
)
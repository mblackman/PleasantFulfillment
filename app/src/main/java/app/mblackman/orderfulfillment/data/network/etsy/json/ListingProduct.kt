package app.mblackman.orderfulfillment.data.network.etsy.json

import com.squareup.moshi.Json

/**
 * Represents a product for listing in Etsy.
 */
data class ListingProduct(
    @Json(name = "product_id") val id: Int,
    @Json(name = "property_values") val propertyValues: List<PropertyValue>,
    @Json(name = "sku") val sku: String,
    @Json(name = "offerings") val offerings: List<ListingOffering>,
    @Json(name = "is_deleted") val isDeleted: Boolean
)
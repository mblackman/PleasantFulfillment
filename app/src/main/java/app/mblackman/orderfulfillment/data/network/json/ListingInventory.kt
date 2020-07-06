package app.mblackman.orderfulfillment.data.network.json

import com.squareup.moshi.Json

/**
 * Represents a listing's inventory in Etsy.
 */
data class ListingInventory(
    @Json(name = "products") val products: List<ListingProduct>,
    @Json(name = "price_on_property") val priceOnProperty: List<Int>,
    @Json(name = "quantity_on_property") val quantityOnProperty: List<Int>,
    @Json(name = "sku_on_property") val skuOnProperty: List<Int>
)
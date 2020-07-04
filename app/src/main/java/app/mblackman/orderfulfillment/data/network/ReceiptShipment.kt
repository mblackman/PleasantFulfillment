package app.mblackman.orderfulfillment.data.network

import com.squareup.moshi.Json

/**
 * The shipment information for a receipt.
 */
data class ReceiptShipment(
    @Json(name = "receipt_shipping_id") val id: String,
    @Json(name = "buyer_user_id") val buyerUserId: String,
    @Json(name = "tracking_code") val trackingCode: String,
    @Json(name = "tracking_url") val trackingUrl: String,
    @Json(name = "carrier_name") val carrierName: String
)
package app.mblackman.orderfulfillment.data.network.etsy.json

import com.squareup.moshi.Json

/**
 * The shipment information for a receipt.
 */
data class ReceiptShipment(
    @Json(name = "carrier_name") val carrierName: String? = null,
    @Json(name = "receipt_shipping_id") val id: Int,
    @Json(name = "tracking_code") val trackingCode: String? = null,
    @Json(name = "tracking_url") val trackingUrl: String? = null,
    @Json(name = "buyer_note") val buyerNote: String? = null,
    @Json(name = "notification_date") val notificationDate: Int? = null
)
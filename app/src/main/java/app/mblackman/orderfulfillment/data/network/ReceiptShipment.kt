package app.mblackman.orderfulfillment.data.network

import com.squareup.moshi.Json

/**
 * The shipment information for a receipt.
 */
data class ReceiptShipment(
    @Json(name = "carrier_name") val carrierName: String,
    @Json(name = "receipt_shipping_id") val id: Int,
    @Json(name = "tracking_code") val trackingCode: String,
    @Json(name = "tracking_url") val trackingUrl: String,
    @Json(name = "buyer_note") val buyerNote: String,
    @Json(name = "notification_date") val notificationDate: Int
)
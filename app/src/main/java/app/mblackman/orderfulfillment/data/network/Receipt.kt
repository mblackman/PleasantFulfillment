package app.mblackman.orderfulfillment.data.network

import com.squareup.moshi.Json

/**
 * A receipt from an Etsy transaction.
 */
data class Receipt(
    @Json(name = "receipt_id") val id: String,
    @Json(name = "buyer_user_id") val buyer_user_id: String,
    @Json(name = "shipments") val shipments: List<ReceiptShipment>
)
package app.mblackman.orderfulfillment.data.network.json

import com.squareup.moshi.Json

/**
 * A receipt from an Etsy transaction.
 */
data class Receipt(
    @Json(name = "receipt_id") val id: Int,
    @Json(name = "receipt_type") val type: Int,
    @Json(name = "order_id") val orderId: Int,
    @Json(name = "seller_user_id") val sellerUserId: Int,
    @Json(name = "buyer_user_id") val buyerUserId: Int,
    @Json(name = "creation_tsz") val creationTime: Float,
    @Json(name = "can_refund") val canRefund: Boolean,
    @Json(name = "last_modified_tsz") val lastModifiedTime: Float,
    @Json(name = "name") val name: String,
    @Json(name = "first_line") val addressFirstLine: String,
    @Json(name = "second_line") val addressSecondLine: String,
    @Json(name = "city") val addressCity: String,
    @Json(name = "state") val addressState: String,
    @Json(name = "zip") val addressZip: String,
    @Json(name = "formatted_address") val formattedAddress: String,
    @Json(name = "country_id") val addressCountryId: Int,
    @Json(name = "payment_method") val paymentMethod: String,
    @Json(name = "payment_email") val paymentEmail: String,
    @Json(name = "message_from_seller") val messageFromSeller: String,
    @Json(name = "message_from_buyer") val messageFromBuyer: String,
    @Json(name = "was_paid") val wasPaid: Boolean,
    @Json(name = "total_tax_cost") val totalTaxCost: Float,
    @Json(name = "total_vat_cost") val totalVatCost: Float,
    @Json(name = "total_price") val totalPrice: Float,
    @Json(name = "total_shipping_cost") val totalShippingCost: Float,
    @Json(name = "currency_code") val currencyCode: String,
    @Json(name = "message_from_payment") val messageFromPayment: String,
    @Json(name = "buyer_email") val buyerEmail: String,
    @Json(name = "seller_email") val sellerEmail: String,
    @Json(name = "is_gift") val isGift: Boolean,
    @Json(name = "needs_gift_wrap") val needsGiftWrap: Boolean,
    @Json(name = "gift_message") val giftMessage: String,
    @Json(name = "gift_wrap_price") val giftWrapPrice: Float,
    @Json(name = "discount_amt") val discountAmount: Float,
    @Json(name = "subtotal") val subtotal: Float,
    @Json(name = "grandtotal") val grandTotal: Float,
    @Json(name = "adjusted_grandtotal") val adjustedGrandTotal: Float,
    @Json(name = "buyer_adjusted_grandtotal") val buyerAdjustedGrandTotal: Float,
    @Json(name = "shipments") val shipments: List<ReceiptShipment>
)
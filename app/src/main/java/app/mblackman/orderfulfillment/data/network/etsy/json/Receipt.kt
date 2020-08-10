package app.mblackman.orderfulfillment.data.network.etsy.json

import com.squareup.moshi.Json

/**
 * A receipt from an Etsy transaction.
 */
data class Receipt(
    @Json(name = "receipt_id") val id: Int,
    @Json(name = "receipt_type") val type: Int? = null,
    @Json(name = "order_id") val orderId: Int? = null,
    @Json(name = "seller_user_id") val sellerUserId: Int? = null,
    @Json(name = "buyer_user_id") val buyerUserId: Int? = null,
    @Json(name = "creation_tsz") val creationTime: Float? = null,
    @Json(name = "can_refund") val canRefund: Boolean? = null,
    @Json(name = "last_modified_tsz") val lastModifiedTime: Float? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "first_line") val addressFirstLine: String? = null,
    @Json(name = "second_line") val addressSecondLine: String? = null,
    @Json(name = "city") val addressCity: String? = null,
    @Json(name = "state") val addressState: String? = null,
    @Json(name = "zip") val addressZip: String? = null,
    @Json(name = "formatted_address") val formattedAddress: String? = null,
    @Json(name = "country_id") val addressCountryId: Int? = null,
    @Json(name = "payment_method") val paymentMethod: String? = null,
    @Json(name = "payment_email") val paymentEmail: String? = null,
    @Json(name = "message_from_seller") val messageFromSeller: String? = null,
    @Json(name = "message_from_buyer") val messageFromBuyer: String? = null,
    @Json(name = "was_paid") val wasPaid: Boolean? = null,
    @Json(name = "total_tax_cost") val totalTaxCost: Float? = null,
    @Json(name = "total_vat_cost") val totalVatCost: Float? = null,
    @Json(name = "total_price") val totalPrice: Float? = null,
    @Json(name = "total_shipping_cost") val totalShippingCost: Float? = null,
    @Json(name = "currency_code") val currencyCode: String? = null,
    @Json(name = "message_from_payment") val messageFromPayment: String? = null,
    @Json(name = "buyer_email") val buyerEmail: String? = null,
    @Json(name = "seller_email") val sellerEmail: String? = null,
    @Json(name = "is_gift") val isGift: Boolean? = null,
    @Json(name = "needs_gift_wrap") val needsGiftWrap: Boolean? = null,
    @Json(name = "gift_message") val giftMessage: String? = null,
    @Json(name = "gift_wrap_price") val giftWrapPrice: Float? = null,
    @Json(name = "discount_amt") val discountAmount: Float? = null,
    @Json(name = "subtotal") val subtotal: Float? = null,
    @Json(name = "grandtotal") val grandTotal: Float? = null,
    @Json(name = "adjusted_grandtotal") val adjustedGrandTotal: Float? = null,
    @Json(name = "buyer_adjusted_grandtotal") val buyerAdjustedGrandTotal: Float? = null,
    @Json(name = "shipments") val shipments: List<ReceiptShipment>? = null
)
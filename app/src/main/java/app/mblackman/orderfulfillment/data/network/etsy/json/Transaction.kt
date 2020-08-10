package app.mblackman.orderfulfillment.data.network.etsy.json

import com.squareup.moshi.Json

/**
 * Represents an Etsy transaction.
 */
data class Transaction(
    @Json(name = "transaction_id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "seller_user_id") val sellerUserId: Int,
    @Json(name = "buyer_user_id") val buyerUserId: Int,
    @Json(name = "creation_tsz") val creationTime: Float,
    @Json(name = "paid_tsz") val paidTime: Float,
    @Json(name = "shipped_tsz") val shippedTime: Float,
    @Json(name = "price") val price: Float,
    @Json(name = "currency_code") val currencyCode: String,
    @Json(name = "quantity") val quantity: Int,
    @Json(name = "tags") val tags: List<String>,
    @Json(name = "materials") val materials: List<String>,
    @Json(name = "image_listing_id") val imageListingId: Int,
    @Json(name = "receipt_id") val receiptId: Int,
    @Json(name = "shipping_cost") val shippingCost: Float,
    @Json(name = "is_digital") val isDigital: Boolean,
    @Json(name = "file_data") val fileData: String,
    @Json(name = "listing_id") val listingId: Int,
    @Json(name = "is_quick_sale") val isQuickSale: Boolean,
    @Json(name = "seller_feedback_id") val sellerFeedbackId: Int,
    @Json(name = "buyer_feedback_id") val buyerFeedbackId: Int,
    @Json(name = "transaction_type") val transactionType: String,
    @Json(name = "url") val url: String,
    @Json(name = "variation") val variations: List<ListingInventory>,
    @Json(name = "product_data") val productData: ListingProduct
)
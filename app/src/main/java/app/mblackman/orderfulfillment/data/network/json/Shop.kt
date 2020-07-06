package app.mblackman.orderfulfillment.data.network.json

import com.squareup.moshi.Json

/**
 * Represents information about an Etsy shop.
 */
data class Shop(
    @Json(name = "shop_id") val id: Int,
    @Json(name = "shop_name") val name: String,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "title") val title: String,
    @Json(name = "creation_tsz") val creationTime: Float,
    @Json(name = "announcement") val announcement: String,
    @Json(name = "currency_code") val currencyCode: String,
    @Json(name = "is_vacation") val isVacation: Boolean,
    @Json(name = "vacation_message") val vacationMessage: String,
    @Json(name = "sale_message") val saleMessage: String,
    @Json(name = "digital_sale_message") val digitalSaleMessage: String,
    @Json(name = "last_updated_tsz") val lastUpdatedTime: Float,
    @Json(name = "listing_active_count") val listingActiveCount: Int,
    @Json(name = "digital_listing_count") val digitalListingCount: Int,
    @Json(name = "login_name") val loginName: String,
    @Json(name = "lat") val latitude: Float,
    @Json(name = "lon") val longitude: Float,
    @Json(name = "accepts_custom_requests") val acceptsCustomRequests: Boolean,
    @Json(name = "policy_welcome") val policyWelcome: String,
    @Json(name = "policy_payment") val policyPayment: String,
    @Json(name = "policy_shipping") val policyShipping: String,
    @Json(name = "policy_refunds") val policyRefunds: String,
    @Json(name = "policy_additional") val policyAdditional: String,
    @Json(name = "policy_seller_info") val policySellerInfo: String,
    @Json(name = "policy_updated_tsz") val policyUpdatedTime: Float,
    @Json(name = "policy_has_private_receipt_info") val policyHasPrivateReceiptInfo: Boolean,
    @Json(name = "vacation_autoreply") val vacationAutoreply: String,
    @Json(name = "ga_code") val googleAnalyticsCode: String,
    @Json(name = "url") val url: String,
    @Json(name = "image_url_760x100") val bannerImageUrl: String,
    @Json(name = "num_favorers") val numberFavorers: Int,
    @Json(name = "languages") val languages: List<String>,
    @Json(name = "upcoming_local_event_id") val upcomingLocalEventId: Int,
    @Json(name = "icon_url_fullxfull") val iconUrlFull: String,
    @Json(name = "is_using_structured_policies") val isUsingStructuredPolicies: Boolean,
    @Json(name = "has_onboarded_structured_policies") val hasOnboardedStructuredPolicies: Boolean,
    @Json(name = "has_unstructured_policies") val hasUnstructuredPolicies: Boolean,
    @Json(name = "policy_privacy") val policyPrivacy: String,
    @Json(name = "use_new_inventory_endpoints") val useNewInventoryEndpoints: Boolean,
    @Json(name = "include_dispute_form_link") val includeDisputeFormLink: Boolean
)

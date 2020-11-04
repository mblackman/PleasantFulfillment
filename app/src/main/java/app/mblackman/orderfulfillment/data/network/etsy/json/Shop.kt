package app.mblackman.orderfulfillment.data.network.etsy.json

import com.squareup.moshi.Json

/**
 * Represents information about an Etsy shop.
 */
data class Shop(
    @Json(name = "shop_id") val id: Int,
    @Json(name = "shop_name") val name: String? = null,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "title") val title: String? = null,
    @Json(name = "creation_tsz") val creationTime: Float? = null,
    @Json(name = "announcement") val announcement: String? = null,
    @Json(name = "currency_code") val currencyCode: String? = null,
    @Json(name = "is_vacation") val isVacation: Boolean? = null,
    @Json(name = "vacation_message") val vacationMessage: String? = null,
    @Json(name = "sale_message") val saleMessage: String? = null,
    @Json(name = "digital_sale_message") val digitalSaleMessage: String? = null,
    @Json(name = "last_updated_tsz") val lastUpdatedTime: Float? = null,
    @Json(name = "listing_active_count") val listingActiveCount: Int? = null,
    @Json(name = "digital_listing_count") val digitalListingCount: Int? = null,
    @Json(name = "login_name") val loginName: String? = null,
    @Json(name = "lat") val latitude: Float? = null,
    @Json(name = "lon") val longitude: Float? = null,
    @Json(name = "accepts_custom_requests") val acceptsCustomRequests: Boolean? = null,
    @Json(name = "policy_welcome") val policyWelcome: String? = null,
    @Json(name = "policy_payment") val policyPayment: String? = null,
    @Json(name = "policy_shipping") val policyShipping: String? = null,
    @Json(name = "policy_refunds") val policyRefunds: String? = null,
    @Json(name = "policy_additional") val policyAdditional: String? = null,
    @Json(name = "policy_seller_info") val policySellerInfo: String? = null,
    @Json(name = "policy_updated_tsz") val policyUpdatedTime: Float? = null,
    @Json(name = "policy_has_private_receipt_info") val policyHasPrivateReceiptInfo: Boolean? = null,
    @Json(name = "vacation_autoreply") val vacationAutoreply: String? = null,
    @Json(name = "ga_code") val googleAnalyticsCode: String? = null,
    @Json(name = "url") val url: String? = null,
    @Json(name = "image_url_760x100") val bannerImageUrl: String? = null,
    @Json(name = "num_favorers") val numberFavorers: Int? = null,
    @Json(name = "languages") val languages: List<String>? = null,
    @Json(name = "upcoming_local_event_id") val upcomingLocalEventId: Int? = null,
    @Json(name = "icon_url_fullxfull") val iconUrlFull: String? = null,
    @Json(name = "is_using_structured_policies") val isUsingStructuredPolicies: Boolean? = null,
    @Json(name = "has_onboarded_structured_policies") val hasOnboardedStructuredPolicies: Boolean? = null,
    @Json(name = "has_unstructured_policies") val hasUnstructuredPolicies: Boolean? = null,
    @Json(name = "policy_privacy") val policyPrivacy: String? = null,
    @Json(name = "use_new_inventory_endpoints") val useNewInventoryEndpoints: Boolean? = null,
    @Json(name = "include_dispute_form_link") val includeDisputeFormLink: Boolean? = null
)

package app.mblackman.orderfulfillment.data.network.json

import com.squareup.moshi.Json

/**
 * Represents an Etsy user.
 */
data class User(
    @Json(name = "user_id") val id: Int,
    @Json(name = "login_name") val loginName: String,
    @Json(name = "primary_email") val primaryEmail: String,
    @Json(name = "creation_tsz") val creationTime: Float,
    @Json(name = "referred_by_user_id") val referredByUserId: Int,
    @Json(name = "awaiting_feedback_count") val awaitingFeedbackCount: Int,
    @Json(name = "use_new_inventory_endpoints") val useNewInventoryEndpoints: Boolean
)
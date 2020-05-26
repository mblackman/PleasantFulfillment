package app.mblackman.orderfulfillment.network

import com.squareup.moshi.Json

data class User(
    @Json(name = "user_id") val id: Int
)
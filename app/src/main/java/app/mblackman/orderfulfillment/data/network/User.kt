package app.mblackman.orderfulfillment.data.network

import com.squareup.moshi.Json

data class User(
    @Json(name = "user_id") val id: Int
)
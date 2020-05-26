package app.mblackman.orderfulfillment.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @Json(name = "user_id") val id: Int
) : Parcelable
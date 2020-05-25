package app.mblackman.orderfulfillment.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Receipt(
    @Json(name = "receipt_id") val id: String,
    @Json(name = "buyer_user_id") val buyer_user_id: String
) : Parcelable
package app.mblackman.orderfulfillment.ui.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResult(val status: LoginStatus) : Parcelable
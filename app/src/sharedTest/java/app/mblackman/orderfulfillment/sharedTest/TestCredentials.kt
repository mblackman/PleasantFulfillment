package app.mblackman.orderfulfillment.sharedTest

import app.mblackman.orderfulfillment.data.network.Credential

data class TestCredentials(
    val name: String,
    val password: String
) : Credential
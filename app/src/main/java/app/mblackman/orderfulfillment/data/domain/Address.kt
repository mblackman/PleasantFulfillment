package app.mblackman.orderfulfillment.data.domain

data class Address(
    val name: String,
    val firstLine: String,
    val secondLine: String,
    val city: String,
    val state: String,
    val zip: String
)
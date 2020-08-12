package app.mblackman.orderfulfillment.data.domain

import java.util.*

data class Order(
    val id: Int,
    val description: String,
    val orderDate: Date,
    val buyerName: String,
    val buyerEmail: String,
    val buyerAddress: Address
)
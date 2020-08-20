package app.mblackman.orderfulfillment.data.domain

import java.time.LocalDate

data class Order(
    val id: Int,
    val description: String,
    val orderDate: LocalDate,
    val buyerName: String,
    val buyerEmail: String,
    val buyerAddress: Address,
    val productSales: List<ProductSale>
)
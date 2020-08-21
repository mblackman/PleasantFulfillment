package app.mblackman.orderfulfillment.data.domain

import androidx.lifecycle.MutableLiveData
import app.mblackman.orderfulfillment.data.common.OrderStatus
import java.time.LocalDate

class Order(
    val id: Int,
    val description: String,
    status: OrderStatus,
    val orderDate: LocalDate,
    val buyerName: String,
    val buyerEmail: String,
    val buyerAddress: Address,
    val productSales: List<ProductSale>
) {

    val orderStatus = MutableLiveData(status)

    val totalCost: Double
        get() =
            productSales.sumByDouble { it.product.price?.times(it.quantity.toDouble()) ?: 0.0 }
}
package app.mblackman.orderfulfillment.data.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import java.time.LocalDateTime

class Order(
    val id: Long,
    val description: String,
    status: OrderStatus,
    val orderDate: LocalDateTime,
    val buyerName: String,
    val buyerEmail: String,
    val buyerAddress: Address,
    val properties: Map<String, String>?
) {
    private val _productSales = MutableLiveData<List<ProductSale>>()

    val orderStatus = MutableLiveData(status)

    val productSales: LiveData<List<ProductSale>>
        get() = _productSales
}

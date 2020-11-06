package app.mblackman.orderfulfillment.data.domain

import androidx.lifecycle.MutableLiveData
import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import java.time.LocalDateTime

data class Order(
    val id: Long,
    val description: String,
    val orderStatus: OrderStatus,
    val orderDate: LocalDateTime,
    val buyerName: String,
    val buyerEmail: String,
    val buyerAddress: Address,
    val properties: Map<String, String>?,
    val productSales: List<ProductSale>?
) {
    var isExpanded = MutableLiveData(false)

    fun toggleExpand() {
        isExpanded.postValue(!isExpanded.value!!)
    }
}

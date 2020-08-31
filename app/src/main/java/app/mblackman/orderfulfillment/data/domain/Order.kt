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

    override fun equals(other: Any?): Boolean {
        if (other !is Order) {
            return false
        }

        return this.id == other.id
                && this.description == other.description
                && this.orderStatus.value == other.orderStatus.value
                && this.orderDate == other.orderDate
                && this.buyerName == other.buyerName
                && this.buyerEmail == other.buyerEmail
                && this.buyerAddress == other.buyerAddress
                && this.properties == other.properties
    }

    override fun toString(): String =
        "Order(id=$id, description=$description, status=${orderStatus.value}, orderDate=$orderDate, buyerName=$buyerName, buyerEmail$buyerEmail, buyerAddress=$buyerAddress, properties=${properties.toString()})"

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + orderDate.hashCode()
        result = 31 * result + buyerName.hashCode()
        result = 31 * result + buyerEmail.hashCode()
        result = 31 * result + buyerAddress.hashCode()
        result = 31 * result + (properties?.hashCode() ?: 0)
        return result
    }
}

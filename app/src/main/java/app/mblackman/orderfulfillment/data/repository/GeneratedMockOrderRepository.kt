package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.mblackman.orderfulfillment.data.domain.Order

/**
 * Generates orders to return.
 */
class GeneratedMockOrderRepository(numberOrders: Int = 10) : OrderRepository() {

    private val generatedOrders = MutableLiveData<List<Order>>()

    init {
        val orders = (0..numberOrders).map { Order(it, "Order: $it") }
        generatedOrders.value = orders
    }

    /**
     * Gets the live data collection of the order details.
     *
     * @return A live data with the collection of order details.
     */
    override suspend fun getOrderDetails(): LiveData<List<Order>> = generatedOrders
}
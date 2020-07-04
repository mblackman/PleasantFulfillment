package app.mblackman.orderfulfillment.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.repository.OrderRepository

/**
 * The orders application logic to control user experience.
 */
class OrdersViewModel(orderRepository: OrderRepository) : ViewModel() {

    val orderDetails: LiveData<List<OrderDetails>> = orderRepository.getOrderDetails()
}

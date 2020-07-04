package app.mblackman.orderfulfillment.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.mblackman.orderfulfillment.data.repository.OrderRepository

/**
 * Creates a orders view model.
 */
class OrdersViewModelFactory(
    private val orderRepository: OrderRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrdersViewModel::class.java)) {
            return OrdersViewModel(orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
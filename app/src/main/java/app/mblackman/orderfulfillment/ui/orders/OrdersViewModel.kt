package app.mblackman.orderfulfillment.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.repository.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * The orders application logic to control user experience.
 */
class OrdersViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val orderDetails: LiveData<PagedList<Order>> = orderRepository.orderDetails

    init {
        getCurrentDetails()
    }

    private fun getCurrentDetails() {
        uiScope.launch {
            orderRepository.updateOrderDetails()
        }
    }
}

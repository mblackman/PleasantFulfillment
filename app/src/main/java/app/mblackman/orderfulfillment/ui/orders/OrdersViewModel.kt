package app.mblackman.orderfulfillment.ui.orders

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.repository.OrderRepository
import kotlinx.coroutines.*

/**
 * The orders application logic to control user experience.
 */
class OrdersViewModel @ViewModelInject constructor(private val orderRepository: OrderRepository) :
    ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val orderDetails: LiveData<PagedList<Order>> = orderRepository.orderDetails

    init {
        //getCurrentDetails()
    }

    private fun getCurrentDetails() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                orderRepository.updateOrderDetails()
            }
        }
    }
}

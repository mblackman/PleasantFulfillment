package app.mblackman.orderfulfillment.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.domain.Shop
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

    private val _orderDetails = MutableLiveData<List<Order>>()

    val orderDetails: LiveData<List<Order>>
        get() = _orderDetails

    init {
        getCurrentDetails()
    }

    private fun getCurrentDetails() {
        uiScope.launch {
            val details = orderRepository.getOrderDetails(Shop(1))
            _orderDetails.postValue(details.value)
        }
    }
}

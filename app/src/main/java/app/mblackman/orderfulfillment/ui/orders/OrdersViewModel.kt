package app.mblackman.orderfulfillment.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.mblackman.orderfulfillment.data.database.OrderDetails
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

    private val _orderDetails = MutableLiveData<List<OrderDetails>>()

    val orderDetails: LiveData<List<OrderDetails>>
        get() = _orderDetails

    init {
        getCurrentDetails()
    }

    private fun getCurrentDetails() {
        uiScope.launch {
            val details = orderRepository.getOrderDetails()
            _orderDetails.postValue(details.value)
        }
    }
}

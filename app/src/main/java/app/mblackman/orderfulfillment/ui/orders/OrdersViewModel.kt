package app.mblackman.orderfulfillment.ui.orders

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import app.mblackman.orderfulfillment.dagger.DefaultDispatcher
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.repository.OrderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

/**
 * The orders application logic to control user experience.
 */
class OrdersViewModel @ViewModelInject constructor(
    private val orderRepository: OrderRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) :
    ViewModel() {

    /**
     * Gets the [Order]s.
     */
    val orderDetails: LiveData<PagedList<Order>> = orderRepository.orderDetails

    /**
     * Gets whether the current login is valid.
     */
    val hasValidLogin: LiveData<Boolean>
        get() = orderRepository.hasValidLogin

    private val _isLoadingOrders = MutableLiveData<Boolean>()

    /**
     * Gets whether orders are currently being loaded.
     */
    val isLoadingOrders: LiveData<Boolean>
        get() = _isLoadingOrders

    fun updateCurrentOrderDetails() {
        viewModelScope.launch(defaultDispatcher) {
            try {
                _isLoadingOrders.postValue(true)
                orderRepository.updateOrderDetails()
            } finally {
                _isLoadingOrders.postValue(false)
            }
        }
    }
}

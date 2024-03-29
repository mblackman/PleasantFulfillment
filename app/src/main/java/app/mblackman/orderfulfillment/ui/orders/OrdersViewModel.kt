package app.mblackman.orderfulfillment.ui.orders

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
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
) : ViewModel() {

    /**
     * Gets the [Order]s.
     */
    val orderDetails: LiveData<PagedList<Order>> = orderRepository.orderDetails

    /**
     * Gets whether the current login is valid.
     */
    val hasValidLogin: LiveData<Boolean>
        get() = orderRepository.hasValidSession

    private val _isLoadingOrders = MutableLiveData<Boolean>(false)

    /**
     * Gets whether orders are currently being loaded.
     */
    val isLoadingOrders: LiveData<Boolean>
        get() = _isLoadingOrders


    private val loadingDataMediator = MediatorLiveData<Pair<Boolean?, PagedList<Order>?>>().apply {
        addSource(isLoadingOrders) {
            value = Pair(it, orderDetails.value)
        }
        addSource(orderDetails) {
            value = Pair(isLoadingOrders.value, it)
        }
    }

    /**
     * Gets whether any orders have been loaded.
     */
    val hasNoOrdersLoaded: LiveData<Boolean> = Transformations.map(loadingDataMediator) {
        it.first == false && it.second?.size == 0
    }

    /**
     * Gets the latest orders data and updates the persistent storage.
     */
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

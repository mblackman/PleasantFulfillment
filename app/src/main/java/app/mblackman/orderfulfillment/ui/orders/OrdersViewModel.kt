package app.mblackman.orderfulfillment.ui.orders

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.getDatabase
import app.mblackman.orderfulfillment.data.network.EtsyApiService
import app.mblackman.orderfulfillment.data.network.EtsyServiceGenerator
import app.mblackman.orderfulfillment.data.network.SessionManager
import app.mblackman.orderfulfillment.data.repository.OrderRepositoryImpl
import app.mblackman.orderfulfillment.data.repository.ReceiptToOrderMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * The orders application logic to control user experience.
 */
class OrdersViewModel(private val application: Application) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val orderRepository by lazy {
        val sessionManager = SessionManager(application)
        val apiService = EtsyServiceGenerator(sessionManager)
            .createService(EtsyApiService::class.java)
        OrderRepositoryImpl(apiService, getDatabase(application), ReceiptToOrderMapper(), uiScope)
    }

    val orderDetails: LiveData<List<OrderDetails>> = orderRepository.getOrderDetails()
}

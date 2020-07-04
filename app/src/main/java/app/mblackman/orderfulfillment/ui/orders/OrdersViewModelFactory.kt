package app.mblackman.orderfulfillment.ui.orders

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Creates a orders view model.
 */
class OrdersViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrdersViewModel::class.java)) {
            return OrdersViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package app.mblackman.orderfulfillment.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import app.mblackman.orderfulfillment.network.EtsyApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * The main application logic to control user experience.
 */
class MainViewModel(private val apiService: EtsyApiService) : ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    companion object {
        private const val SHOP_ID = 20140962
    }

    init {
        getUnshippedReceipts()
    }

    private fun getUnshippedReceipts() {
        coroutineScope.launch {

            try {
                val user = apiService.getUserSelfAsync().await().results.first()
                apiService.getUnshippedReceiptsAsync(user.id).await()
            } catch (e: Exception) {
                Log.e(MainViewModel::class.java.name, e.toString())
            }
        }
    }
}

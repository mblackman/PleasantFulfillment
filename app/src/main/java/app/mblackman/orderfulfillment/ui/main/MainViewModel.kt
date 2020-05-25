package app.mblackman.orderfulfillment.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.mblackman.orderfulfillment.network.EtsyApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val apiService: EtsyApiService) : ViewModel() {

    enum class ApiStatus { LOADING, ERROR, DONE }

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    companion object {
        private const val SHOP_ID = 20140962
    }

    init {
        getUnshippedReceipts()
    }

    private fun getUnshippedReceipts() {
        coroutineScope.launch {
            try {
                val receipts = apiService.getUnshippedReceiptsAsync(SHOP_ID).await()
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
            }
        }
    }
}

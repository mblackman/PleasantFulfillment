package app.mblackman.orderfulfillment.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.mblackman.orderfulfillment.network.EtsyApiService

class MainViewModelFactory(
    private val apiService: EtsyApiService
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
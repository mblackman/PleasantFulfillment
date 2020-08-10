package app.mblackman.orderfulfillment.ui.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.mblackman.orderfulfillment.data.network.etsy.SessionManager

/**
 * Creates a new login view model.
 */
class LoginViewModelFactory(
    private val sessionManager: SessionManager,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(sessionManager, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
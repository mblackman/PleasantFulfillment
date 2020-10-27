package app.mblackman.orderfulfillment

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import app.mblackman.orderfulfillment.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Handles retrieving and maintaining login credentials for online endpoint.
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_activity)

        intent?.data?.let {
            loginViewModel.handleRedirectUri(it)
        }
    }
}

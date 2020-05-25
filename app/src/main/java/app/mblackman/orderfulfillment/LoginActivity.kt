package app.mblackman.orderfulfillment

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.mblackman.orderfulfillment.network.SessionManager
import app.mblackman.orderfulfillment.ui.login.LoginViewModel
import app.mblackman.orderfulfillment.ui.login.LoginViewModelFactory

/**
 * Handles retrieving and maintaining login credentials for online endpoint.
 */
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by lazy {
        val viewModelFactory = LoginViewModelFactory(SessionManager(application), application)
        ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = intent?.data
        if (uri != null) {
            // If a uri is passed to the activity, try to handle any callbacks.
            handleUriIntent(uri)
        }

        setContentView(R.layout.login_activity)

        loginViewModel.authorizationUrl.observe(this, Observer {
            it?.let {
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder
                    .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .build()

                customTabsIntent.launchUrl(this, Uri.parse(it))
            }
        })

        loginViewModel.getAuthorization()
    }

    // Tries to parse expected uris. Will handle callbacks.
    private fun handleUriIntent(uri: Uri) {
        if (uri.toString().startsWith(getString(R.string.etsy_login_callback_uri))) {
            val verifier = uri.getQueryParameter("oauth_verifier")
            if (!TextUtils.isEmpty(verifier)) {
                loginViewModel.setAccessToken(verifier!!)
            }
        }
    }
}

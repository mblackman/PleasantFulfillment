package app.mblackman.orderfulfillment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
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

        val uri = intent?.data
        if (uri != null) {
            // If a uri is passed to the activity, try to handle any callbacks.
            handleUriIntent(uri)
        }

        setContentView(R.layout.login_activity)

        loginViewModel.authorizationUrl.observe(this) {
            it?.let {
                val parsedUri: Uri = Uri.parse(it)
                val intent = Intent(Intent.ACTION_VIEW, parsedUri)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }

        loginViewModel.getAuthorization()
    }

    // Tries to parse expected uris. Will handle callbacks.
    private fun handleUriIntent(uri: Uri) {
        if (uri.toString().startsWith(BuildConfig.ETSY_API_REDIRECT)) {
            val verifier = uri.getQueryParameter("oauth_verifier")
            if (!TextUtils.isEmpty(verifier)) {
                loginViewModel.setAccessToken(verifier!!)
            }
        }
    }
}

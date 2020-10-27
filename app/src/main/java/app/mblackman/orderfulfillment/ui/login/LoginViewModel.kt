package app.mblackman.orderfulfillment.ui.login

import android.net.Uri
import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.mblackman.orderfulfillment.BuildConfig
import app.mblackman.orderfulfillment.data.network.CredentialManager
import app.mblackman.orderfulfillment.data.network.CredentialSource
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Handles authenticating and storing credentials to web endpoints.
 *
 * @param credentialManager The session manager to manage credentials with.
 */
class LoginViewModel @ViewModelInject constructor(
    private val credentialManager: CredentialManager
) : ViewModel() {

    companion object {
        private const val EtsyRedirectUri = "app.mblackman.orderfulfillment://etsylogincallback"
    }

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _authorizationUrl = MutableLiveData<String>()

    /**
     * Gets the url to the authorization page.
     */
    val authorizationUrl: LiveData<String>
        get() = _authorizationUrl

    /**
     * Handles the given login redirect url.
     *
     * @param uri The redirect [Uri].
     */
    fun handleRedirectUri(uri: Uri) {
        if (uri.toString().startsWith(EtsyRedirectUri)) {
            val verifier = uri.getQueryParameter("oauth_verifier")
            if (!TextUtils.isEmpty(verifier)) {
                saveEtsyAccessToken(verifier!!)
            }
        }
    }

    private fun saveEtsyAccessToken(verifier: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                getEtsyLoginService().getAccessToken(verifier)?.let {
                    credentialManager.storeCredential(it, CredentialSource.Etsy)
                }
            }
        }
    }

    /**
     * Gets the login page for Etsy and updates [authorizationUrl].
     */
    fun startEtsyLogin() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val authPage = getEtsyLoginService().getAuthorizationPage()

                if (authPage == null) {
                    Timber.e("Could not load authorization url for Etsy.")
                }

                _authorizationUrl.postValue(authPage)
            }
        }
    }

    private fun getEtsyLoginService() =
        OAuth1RedirectLogin(
            BuildConfig.ETSY_CONSUMER_KEY,
            BuildConfig.ETSY_CONSUMER_SECRET,
            EtsyRedirectUri,
            CredentialSource.Etsy,
            credentialManager
        )
}

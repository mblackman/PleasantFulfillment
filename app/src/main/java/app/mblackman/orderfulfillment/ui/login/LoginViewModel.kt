package app.mblackman.orderfulfillment.ui.login

import android.net.Uri
import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mblackman.orderfulfillment.BuildConfig
import app.mblackman.orderfulfillment.dagger.DefaultDispatcher
import app.mblackman.orderfulfillment.data.network.CredentialManager
import app.mblackman.orderfulfillment.data.network.CredentialSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Handles authenticating and storing credentials to web endpoints.
 *
 * @param credentialManager The session manager to manage credentials with.
 * @param etsyRedirectLogin Handles redirect urls and getting access token.
 */
class LoginViewModel @ViewModelInject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val credentialManager: CredentialManager,
    private val etsyRedirectLogin: RedirectLogin
) : ViewModel() {
    private val _authorizationUrl = MutableLiveData<String>()

    /**
     * Gets the url to the authorization page.
     */
    val authorizationUrl: LiveData<String>
        get() = _authorizationUrl

    private val _loginStatus = MutableLiveData<LoginStatus>()

    /**
     * Gets any messages from the view model.
     */
    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus

    /**
     * Handles the given login redirect url.
     *
     * @param uri The redirect [Uri].
     */
    fun handleRedirectUri(uri: Uri) {
        if (uri.toString().startsWith(BuildConfig.ETSY_REDIRECT_URL)) {
            val verifier = uri.getQueryParameter("oauth_verifier")
            if (!TextUtils.isEmpty(verifier)) {
                saveEtsyAccessToken(verifier!!)
            }
        }
    }

    private fun saveEtsyAccessToken(verifier: String) {
        viewModelScope.launch(defaultDispatcher) {
            etsyRedirectLogin.getAccessToken(verifier)?.let {
                credentialManager.storeCredential(it, CredentialSource.Etsy)
            }
        }
    }

    /**
     * Gets the login page for Etsy and updates [authorizationUrl].
     */
    fun startEtsyLogin() {
        viewModelScope.launch(defaultDispatcher) {
            val authPage = etsyRedirectLogin.getAuthorizationPage()

            if (authPage == null) {
                Timber.e("Could not load authorization url for Etsy.")
                _loginStatus.postValue(LoginStatus.GET_AUTHORIZATION_PAGE_FAILED)
            } else {
                _authorizationUrl.postValue(authPage)
                _loginStatus.postValue(LoginStatus.GET_AUTHORIZATION_PAGE_SUCCESS)
            }
        }
    }
}

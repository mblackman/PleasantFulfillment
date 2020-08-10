package app.mblackman.orderfulfillment.ui.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.mblackman.orderfulfillment.BuildConfig
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.data.network.etsy.SessionManager
import com.github.scribejava.apis.EtsyApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1RequestToken
import kotlinx.coroutines.*

/**
 * Handles authenticating and storing credentials to web endpoints.
 *
 * @param sessionManager The session manager to manage credentials with.
 * @param application The application context.
 */
class LoginViewModel(
    private val sessionManager: SessionManager,
    private val application: Application
) : ViewModel() {

    /**
     * The status of the login process.
     */
    enum class LoginStatus { GETTING_AUTH_URL, AWAITING_OAUTH_VERIFIER, ERROR, DONE }

    private var prefs: SharedPreferences = application.getSharedPreferences(
        application.getString(R.string.app_name),
        Context.MODE_PRIVATE
    )

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val apiService = ServiceBuilder(BuildConfig.ETSY_CONSUMER_KEY)
        .apiSecret(BuildConfig.ETSY_CONSUMER_SECRET)
        .callback(application.getString(R.string.etsy_login_callback_uri))
        .build(EtsyApi.instance("transactions_r"))

    private val _authorizationUrl = MutableLiveData<String>()

    /**
     * Gets the authorization url to load.
     */
    val authorizationUrl: LiveData<String>
        get() = _authorizationUrl

    private val _loginStatus = MutableLiveData<LoginStatus>(LoginStatus.GETTING_AUTH_URL)

    /**
     * Gets the login status.
     */
    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus

    companion object {
        private const val TEMP_TOKEN_SECRET_PREF_NAME = "etsy_temp_token_secret"
        private const val TEMP_TOKEN_PREF_NAME = "etsy_temp_token"
        private const val TEMP_RAW_PREF_NAME = "etsy_temp_raw"

    }

    /**
     * Gets the access token from the api using the given OAuth validation.
     *
     * @param oauthVerifier The OAuth verification string.
     */
    fun setAccessToken(oauthVerifier: String) {
        uiScope.launch {
            getAccessToken(oauthVerifier)
        }
    }

    private suspend fun getAccessToken(oauthVerifier: String) {
        withContext(Dispatchers.IO) {
            val requestToken = getSavedOAuthToken()
            val accessToken = apiService.getAccessToken(requestToken, oauthVerifier)
            if (accessToken.isEmpty) {
                // No access token retrieved.
                _loginStatus.postValue(LoginStatus.GETTING_AUTH_URL)

                uiScope.launch {
                    getAuthorizationPage()
                }
            } else {
                // Retrieved access token
                sessionManager.saveAuthToken(accessToken.token, accessToken.tokenSecret)
                _loginStatus.postValue(LoginStatus.DONE)
            }
        }
    }

    /**
     * Tries to get the store authorization, else loads the login web page.
     */
    fun getAuthorization() {
        val existingAccessKey = sessionManager.fetchAuthToken()

        if (TextUtils.isEmpty(existingAccessKey)) {
            // Haven't retrieved an access token yet.
            uiScope.launch {
                getAuthorizationPage()
            }
        } else {
            _loginStatus.value = LoginStatus.DONE
        }
    }

    private suspend fun getAuthorizationPage() {
        withContext(Dispatchers.IO) {
            try {
                val requestToken = apiService.requestToken
                val authUrl = apiService.getAuthorizationUrl(requestToken)
                setSavedOAuthToken(requestToken)
                _authorizationUrl.postValue(authUrl)
                _loginStatus.postValue(LoginStatus.AWAITING_OAUTH_VERIFIER)
            } catch (e: Exception) {
                _loginStatus.postValue(LoginStatus.ERROR)
                throw e
            }
        }
    }

    private fun setSavedOAuthToken(requestToken: OAuth1RequestToken) {
        prefs.edit()
            .putString(TEMP_TOKEN_SECRET_PREF_NAME, requestToken.tokenSecret)
            .putString(TEMP_TOKEN_PREF_NAME, requestToken.token)
            .putString(TEMP_RAW_PREF_NAME, requestToken.rawResponse)
            .apply()
    }

    private fun getSavedOAuthToken(): OAuth1RequestToken {
        val token = prefs.getString(TEMP_TOKEN_PREF_NAME, "")
        val secret = prefs.getString(TEMP_TOKEN_SECRET_PREF_NAME, "")
        val raw = prefs.getString(TEMP_RAW_PREF_NAME, "")
        val requestToken = OAuth1RequestToken(token, secret, raw)
        prefs.edit()
            .remove(TEMP_TOKEN_PREF_NAME)
            .remove(TEMP_TOKEN_SECRET_PREF_NAME)
            .remove(TEMP_RAW_PREF_NAME)
            .apply()

        return requestToken
    }
}

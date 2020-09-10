package app.mblackman.orderfulfillment.ui.login

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import app.mblackman.orderfulfillment.BuildConfig
import app.mblackman.orderfulfillment.data.network.etsy.SessionManager
import com.github.scribejava.apis.EtsyApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1RequestToken
import kotlinx.coroutines.*

/**
 * Handles authenticating and storing credentials to web endpoints.
 *
 * @param sessionManager The session manager to manage credentials with.
 */
class LoginViewModel @ViewModelInject constructor(
    private val sessionManager: SessionManager,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * The status of the login process.
     */
    enum class LoginStatus { GETTING_AUTH_URL, AWAITING_OAUTH_VERIFIER, ERROR, DONE }

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val apiService = ServiceBuilder(BuildConfig.ETSY_CONSUMER_KEY)
        .apiSecret(BuildConfig.ETSY_CONSUMER_SECRET)
        .callback(BuildConfig.ETSY_API_REDIRECT)
        .build(EtsyApi.instance("transactions_r"))

    private val _authorizationUrl = MutableLiveData<String>()

    /**
     * Gets the authorization url to load.
     */
    val authorizationUrl: LiveData<String>
        get() = _authorizationUrl

    private val _loginStatus = MutableLiveData(LoginStatus.GETTING_AUTH_URL)

    /**
     * Gets the login status.
     */
    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus

    companion object {
        private const val TEMP_TOKEN_KEY = "etsy_temp_token"
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
            val requestToken = getSavedOAuthToken() ?: return@withContext
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
        savedStateHandle.set(
            TEMP_TOKEN_KEY,
            TempToken(requestToken.tokenSecret, requestToken.token, requestToken.rawResponse)
        )
    }

    private fun getSavedOAuthToken(): OAuth1RequestToken? =
        savedStateHandle.get<TempToken>(TEMP_TOKEN_KEY)?.asOAuth1RequestToken()

    data class TempToken(val secret: String?, val token: String?, val rawResponse: String?) :
        Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        )

        fun asOAuth1RequestToken(): OAuth1RequestToken? {
            if (secret != null && token != null && rawResponse != null) {
                return OAuth1RequestToken(token, secret, rawResponse)
            }
            return null
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(secret)
            parcel.writeString(token)
            parcel.writeString(rawResponse)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<TempToken> {
            override fun createFromParcel(parcel: Parcel): TempToken {
                return TempToken(parcel)
            }

            override fun newArray(size: Int): Array<TempToken?> {
                return arrayOfNulls(size)
            }
        }
    }
}

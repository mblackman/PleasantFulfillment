package app.mblackman.orderfulfillment.data.network.etsy

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.mblackman.orderfulfillment.R
import javax.inject.Inject

/**
 * Managers online sessions with api endpoints.
 *
 * @param context The context of the constructing caller.
 */
class SessionManager @Inject constructor(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    /**
     * Gets whether the session has valid connections.
     */
    val isSessionValid: Boolean
        get() = fetchAuthToken() != null

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_TOKEN_SECRET = "user_token_secret"
        private val authenticationChangedInternal = MutableLiveData<Boolean>()
        val authenticationChanged: LiveData<Boolean>
            get() = authenticationChangedInternal
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String, secret: String) {
        prefs.edit()
            .putString(USER_TOKEN, token)
            .putString(USER_TOKEN_SECRET, secret)
            .apply()

        authenticationChangedInternal.postValue(true)
    }

    /**
     * Clears the auth token from the session.
     */
    fun clearAuthToken() {
        prefs.edit()
            .remove(USER_TOKEN)
            .remove(USER_TOKEN_SECRET)
            .apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthTokenSecret(): String? {
        return prefs.getString(USER_TOKEN_SECRET, null)
    }

    /**
     * Call whenever authentication has failed for the stored credentials.
     */
    fun onAuthenticationFailed() {
        authenticationChangedInternal.postValue(false)
    }
}
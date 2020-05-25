package app.mblackman.orderfulfillment.network

import android.content.Context
import android.content.SharedPreferences
import app.mblackman.orderfulfillment.R

/**
 * Managers online sessions with api endpoints.
 *
 * @param context The context of the constructing caller.
 */
class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    /**
     * Gets whether the session has valid connections.
     */
    val isSessionValid: Boolean
        get() = fetchAuthToken() != null

    companion object {
        const val USER_TOKEN = "user_token"
        private var authenticationListener: AuthenticationListener? = null
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /**
     * Sets the authentication listener to notify listeners.
     *
     * @param listener The listener to set.
     */
    fun setAuthenticationListener(listener: AuthenticationListener) {
        authenticationListener = listener
    }

    /**
     * Call whenever authentication has failed for the stored credentials.
     */
    fun onAuthenticationFailed() {
        prefs.edit().remove(USER_TOKEN).apply()
        authenticationListener?.onAuthenticationFailed()
    }
}
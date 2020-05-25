package app.mblackman.orderfulfillment.network

import android.content.Context
import android.content.SharedPreferences
import app.mblackman.orderfulfillment.R

class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

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

    fun setAuthenticationListener(listener: AuthenticationListener) {
        authenticationListener = listener
    }

    fun onAuthenticationFailed() {
        authenticationListener?.onAuthenticationFailed()
    }
}
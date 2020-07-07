package app.mblackman.orderfulfillment.data

import android.content.Context
import android.content.SharedPreferences
import app.mblackman.orderfulfillment.R

/**
 * Uses shared preferences to persist configuration settings.
 */
class SharedPreferencesConfiguration(context: Context) : Configuration {

    private val CURRENT_USER_ID_PROPERTY_NAME = "CURRENT_USER_ID"

    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    /**
     * Gets or sets the id of the application user.
     */
    override var currentUserId: Int?
        get() = prefs.getInt(CURRENT_USER_ID_PROPERTY_NAME, 0)
        set(value) {
            if (value == null) {
                prefs.edit().remove(CURRENT_USER_ID_PROPERTY_NAME).apply()
            } else {
                prefs.edit().putInt(CURRENT_USER_ID_PROPERTY_NAME, value).apply()
            }
        }
}
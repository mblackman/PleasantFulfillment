package app.mblackman.orderfulfillment.data.network.etsy

import android.content.Context
import android.content.SharedPreferences
import app.mblackman.orderfulfillment.R
import javax.inject.Inject

/**
 * Uses shared preferences to persist configuration settings.
 */
class SharedPreferencesConfiguration @Inject constructor(context: Context) : Configuration {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        private const val CURRENT_USER_ID_PROPERTY_NAME = "CURRENT_USER_ID"
        private const val CURRENT_USER_SHOP_ID_PROPERTY_NAME = "CURRENT_USER_SHOP_ID"
    }

    /**
     * Gets or sets the id of the application user.
     */
    override var currentUserId: Int?
        get() = getInt(CURRENT_USER_ID_PROPERTY_NAME)
        set(value) {
            setInt(CURRENT_USER_ID_PROPERTY_NAME, value)
        }

    override var currentUserShopId: Int?
        get() = getInt(CURRENT_USER_SHOP_ID_PROPERTY_NAME)
        set(value) {
            setInt(CURRENT_USER_SHOP_ID_PROPERTY_NAME, value)
        }

    private fun getInt(keyName: String) =
        if (prefs.contains(keyName)) prefs.getInt(keyName, 0) else null

    private fun setInt(keyName: String, value: Int?) {
        if (value == null) {
            prefs.edit().remove(keyName).apply()
        } else {
            prefs.edit().putInt(keyName, value).apply()
        }
    }
}
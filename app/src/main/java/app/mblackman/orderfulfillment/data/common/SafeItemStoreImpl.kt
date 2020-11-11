package app.mblackman.orderfulfillment.data.common

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import app.mblackman.orderfulfillment.R
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Provides persistent data storage that cannot be accessed by external processes.
 */
class SafeItemStoreImpl @Inject constructor(private val context: Context) : SafeItemStore {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    /**
     * Gets a item for the given key.
     *
     * @param key The key to look up the item by.
     * @param itemClass The class type of the item to get.
     * @return The item if found, else null.
     */
    override fun <T : Any> getItem(key: String, itemClass: KClass<out T>): T? {
        getSharedPreferences().getString(key, null)?.let {
            val jsonAdapter: JsonAdapter<T> = moshi.adapter(itemClass.java) as JsonAdapter<T>
            return jsonAdapter.fromJson(it)
        }

        return null
    }

    /**
     * Stores the given item with for the given key
     *
     * @param key The key to store the item under.
     * @param item The item to store.
     */
    override fun storeItem(key: String, item: Any) {
        val jsonAdapter = moshi.adapter(item.javaClass)
        val json = jsonAdapter.toJson(item)

        getSharedPreferences().edit()
            .putString(key, json)
            .apply()
    }

    /**
     * Clears any item for the given key.
     *
     * @param key The key for the item to clear.
     */
    override fun clearItem(key: String) {
        getSharedPreferences().edit()
            .remove(key)
            .apply()
    }

    /**
     * Gets an instance of [SharedPreferences].
     */
    private fun getSharedPreferences(): SharedPreferences {
        val keyAlias = "credentialManager"
        val masterKey = MasterKey.Builder(context, keyAlias)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            context.getString(R.string.app_name),
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}
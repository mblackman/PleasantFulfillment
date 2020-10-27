package app.mblackman.orderfulfillment.data.network

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
 * Stores and retrieves credentials of different types and sources.
 *
 * @param context The context of the where this is constructed.
 */
class CredentialManagerImpl @Inject constructor(private val context: Context) : CredentialManager {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    /**
     * Gets a [Credential] for the given source.
     *
     * @param credentialClass The class type of the [Credential] to get.
     * @param source The data source to get a [Credential] for.
     * @return The credential if exists, else null.
     */
    override fun <T : Credential> getCredential(
        credentialClass: KClass<out T>,
        source: CredentialSource
    ): T? {
        getSharedPreferences().getString(getKey(credentialClass, source), null)?.let {
            val jsonAdapter: JsonAdapter<T> = moshi.adapter(credentialClass.java) as JsonAdapter<T>
            return jsonAdapter.fromJson(it)
        }

        return null
    }

    /**
     * Stores the given [Credential] with its [CredentialSource].
     *
     * @param credential The credential data to store.
     * @param source The data source for the [Credential] to store.
     */
    override fun <T : Credential> storeCredential(credential: T, source: CredentialSource) {
        val jsonAdapter: JsonAdapter<T> = moshi.adapter(credential.javaClass)
        val json = jsonAdapter.toJson(credential)
        getSharedPreferences().edit()
            .putString(getKey(credential::class, source), json)
            .apply()
    }

    private fun getKey(credentialClass: KClass<out Credential>, source: CredentialSource) =
        credentialClass.qualifiedName + source.storageName

    /**
     * Gets an instance of [SharedPreferences] for this [CredentialManagerImpl].
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
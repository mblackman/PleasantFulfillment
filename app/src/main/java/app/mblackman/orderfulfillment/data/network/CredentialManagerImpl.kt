package app.mblackman.orderfulfillment.data.network

import app.mblackman.orderfulfillment.data.common.SafeItemStore
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Stores and retrieves credentials of different types and sources.
 *
 * @param safeItemStore The [SafeItemStore] to persist credentials in.
 */
class CredentialManagerImpl @Inject constructor(private val safeItemStore: SafeItemStore) :
    CredentialManager {
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
    ): T? = safeItemStore.getItem(getKey(credentialClass, source), credentialClass)

    /**
     * Stores the given [Credential] with its [CredentialSource].
     *
     * @param credential The credential data to store.
     * @param source The data source for the [Credential] to store.
     */
    override fun <T : Credential> storeCredential(credential: T, source: CredentialSource) {
        safeItemStore.storeItem(getKey(credential::class, source), credential)
    }

    /**
     * Clears any credentials for the given credential type and source.
     *
     * @param credentialClass The [KClass] of the credential to clear.
     * @param source The source of the credential.
     */
    override fun <T : Credential> clearCredential(
        credentialClass: KClass<out T>,
        source: CredentialSource
    ) {
        safeItemStore.clearItem(getKey(credentialClass, source))
    }

    private fun getKey(credentialClass: KClass<out Credential>, source: CredentialSource) =
        credentialClass.qualifiedName + source.storageName
}
package app.mblackman.orderfulfillment.data.network

import kotlin.reflect.KClass

/**
 * The data sources that [CredentialManagerImpl] manages [Credential]s for.
 */
enum class CredentialSource(val storageName: String) {
    None("NONE"), Etsy("ETSY")
}

/**
 * A utility to persist and retrieve [Credential]s.
 */
interface CredentialManager {
    /**
     * Gets a [Credential] for the given source.
     *
     * @param credentialClass The class type of the [Credential] to get.
     * @param source The data source to get a [Credential] for.
     * @return The credential if exists, else null.
     */
    fun <T : Credential> getCredential(credentialClass: KClass<out T>, source: CredentialSource): T?

    /**
     * Stores the given [Credential] with its [CredentialSource].
     *
     * @param credential The credential data to store.
     * @param source The data source for the [Credential] to store.
     */
    fun <T : Credential> storeCredential(credential: T, source: CredentialSource)

    /**
     * Clears any credentials for the given credential type and source.
     *
     * @param credentialClass The [KClass] of the credential to clear.
     * @param source The source of the credential.
     */
    fun <T : Credential> clearCredential(credentialClass: KClass<out T>, source: CredentialSource)
}

/**
 * Gets a [Credential] for the given [CredentialSource].
 *
 * @param source The data source to get a [Credential] for.
 * @return The credential if exists, else null.
 */
inline fun <reified T : Credential> CredentialManager.getCredential(source: CredentialSource): T? =
    getCredential(T::class, source)

/**
 * Clears the stored [Credential] for the given source.
 *
 * @param source The data source to clear ta [Credential] for.
 */
inline fun <reified T : Credential> CredentialManager.clearCredential(source: CredentialSource) =
    clearCredential(T::class, source)
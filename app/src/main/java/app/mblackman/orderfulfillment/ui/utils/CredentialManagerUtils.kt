package app.mblackman.orderfulfillment.ui.utils

import app.mblackman.orderfulfillment.data.network.*

/**
 * Gets the Etsy [Credential] if they exist, else null.
 */
fun CredentialManager.getEtsyLogin(): OAuthCredential? =
    this.getCredential(CredentialSource.Etsy)

/**
 * Checks whether a [Credential] for Etsy exists.
 */
fun CredentialManager.hasEtsyLogin(): Boolean = this.getEtsyLogin() != null

/**
 * Stores the given [Credential] for Etsy.
 *
 * @param credential The [Credential] to store.
 */
fun CredentialManager.storeEtsyCredential(credential: Credential) {
    this.storeCredential(credential, CredentialSource.Etsy)
}

/**
 * Clears any stored Etsy [Credential].
 */
fun CredentialManager.clearEtsyLogin() {
    this.clearCredential<OAuthCredential>(CredentialSource.Etsy)
}
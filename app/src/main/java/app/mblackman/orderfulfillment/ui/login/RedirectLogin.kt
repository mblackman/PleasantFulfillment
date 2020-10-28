package app.mblackman.orderfulfillment.ui.login

import app.mblackman.orderfulfillment.data.network.Credential

/**
 * Handles a login redirect for getting tokens from a web source.
 */
interface RedirectLogin {
    /**
     * Gets the access token from the OAuth source.
     */
    fun getAccessToken(oauthVerifier: String): Credential?

    /**
     * Gets the url to the login page for OAuth.
     */
    fun getAuthorizationPage(): String?
}
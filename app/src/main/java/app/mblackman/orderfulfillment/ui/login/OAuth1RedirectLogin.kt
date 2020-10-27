package app.mblackman.orderfulfillment.ui.login

import app.mblackman.orderfulfillment.data.network.*
import com.github.scribejava.apis.EtsyApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1RequestToken
import timber.log.Timber

/**
 * Handles redirect logic for OAuth 1.0.
 */
class OAuth1RedirectLogin(
    consumerKey: String,
    consumerSecret: String,
    callbackUri: String,
    private val credentialSource: CredentialSource,
    private val credentialManager: CredentialManager
) {
    private val apiService = ServiceBuilder(consumerKey)
        .apiSecret(consumerSecret)
        .callback(callbackUri)
        .build(
            when (credentialSource) {
                CredentialSource.Etsy -> EtsyApi.instance("transactions_r")
                else -> throw Exception("Unknown type of source " + credentialSource.storageName)
            }
        )

    /**
     * Gets the access token from the OAuth source.
     */
    fun getAccessToken(oauthVerifier: String): Credential? {
        val requestToken =
            credentialManager.getCredential<TempToken>(credentialSource)?.asOAuth1RequestToken()

        if (requestToken == null) {
            Timber.e("Could not find saved OAuth token.")
            return null
        }

        val accessToken = apiService.getAccessToken(requestToken, oauthVerifier)

        return if (accessToken.isEmpty) {
            Timber.w("Failed to get access token for service: ${credentialSource.storageName}")
            null
        } else {
            // Retrieved access token
            OAuthCredential(
                requestToken.token,
                requestToken.tokenSecret,
                accessToken.token,
                accessToken.tokenSecret
            )
        }
    }

    /**
     * Gets the url to the login page for OAuth.
     */
    fun getAuthorizationPage(): String? {
        try {
            val requestToken = apiService.requestToken
            val authUrl = apiService.getAuthorizationUrl(requestToken)

            credentialManager.storeCredential(
                TempToken(requestToken.tokenSecret, requestToken.token, requestToken.rawResponse),
                credentialSource
            )

            return authUrl
        } catch (e: Exception) {
            Timber.e(e)
        }

        return null
    }
}


private data class TempToken(val secret: String?, val token: String?, val rawResponse: String?) :
    Credential {
    fun asOAuth1RequestToken(): OAuth1RequestToken? {
        if (secret != null && token != null && rawResponse != null) {
            return OAuth1RequestToken(token, secret, rawResponse)
        }
        return null
    }
}
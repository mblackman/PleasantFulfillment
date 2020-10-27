package app.mblackman.orderfulfillment.data.network.etsy

import app.mblackman.orderfulfillment.data.network.CredentialManager
import app.mblackman.orderfulfillment.data.network.CredentialSource
import app.mblackman.orderfulfillment.data.network.OAuthCredential
import app.mblackman.orderfulfillment.data.network.getCredential
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import java.io.IOException

/**
 * Intercepts requests to the Esty Api and modifies the url.
 *
 * @param credentialManager Holds the credential data, including access tokens and secrets.
 */
class EtsyApiInterceptor(
    private val credentialManager: CredentialManager
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()

        // Add access token to request if set.
        val credential = credentialManager.getCredential<OAuthCredential>(CredentialSource.Etsy)

        if (credential != null) {
            val requestBuilder = original.newBuilder()

            // If the access token and secret are set, add the authorization to the header.
            with(OkHttpOAuthConsumer(credential.consumerKey, credential.consumerSecret)) {
                setTokenWithSecret(credential.token, credential.tokenSecret)
                val request = sign(requestBuilder.build()).unwrap() as Request
                return chain.proceed(request)
            }
        } else {
            return chain.proceed(original)
        }
    }
}
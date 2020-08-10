package app.mblackman.orderfulfillment.data.network.etsy

import android.text.TextUtils
import app.mblackman.orderfulfillment.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import java.io.IOException

/**
 * Intercepts requests to the Esty Api and modifies the url.
 *
 * @param sessionManager Holds the session data, including access tokens and secrets.
 */
class EtsyApiInterceptor(
    private val sessionManager: SessionManager
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val requestBuilder = original.newBuilder()
        val request: Request

        // Add access token to request if set.
        val authToken = sessionManager.fetchAuthToken()
        val authTokenSecret = sessionManager.fetchAuthTokenSecret()

        if (!TextUtils.isEmpty(authToken) && !TextUtils.isEmpty(authTokenSecret)) {
            // If the access token and secret are set, add the authorization to the header.
            val consumer =
                OkHttpOAuthConsumer(BuildConfig.ETSY_CONSUMER_KEY, BuildConfig.ETSY_CONSUMER_SECRET)
            consumer.setTokenWithSecret(authToken, authTokenSecret)
            request = consumer.sign(requestBuilder.build()).unwrap() as Request
        } else {
            sessionManager.onAuthenticationFailed()
            return chain.proceed(original)
        }

        val response = chain.proceed(request)

        if (response.code() == 403) {
            sessionManager.onAuthenticationFailed()
        }

        return response
    }
}
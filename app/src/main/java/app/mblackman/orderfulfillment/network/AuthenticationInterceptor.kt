package app.mblackman.orderfulfillment.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Intercepts requests to the Esty Api and adds any authorization data.
 *
 * @param sessionManager Holds the session data, including access tokens and secrets.
 * @param apiKey The api key for the etsy api.
 */
class AuthenticationInterceptor(
    private val sessionManager: SessionManager,
    private val apiKey: String
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val originalHttpUrl = original.url()

        // Add api key query parameter.
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()

        // Create new request builder with updated url.
        val requestBuilder = original.newBuilder()
            .url(url)

        // Add access token to request if set.
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.header("Authorization", it)
        }

        val request = requestBuilder.build()
        val response = chain.proceed(request)

        if (response.code() == 403) {
            sessionManager.onAuthenticationFailed()
            return response
        }

        return response
    }

}
package app.mblackman.orderfulfillment.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


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
package app.mblackman.orderfulfillment.network

import android.content.Context
import app.mblackman.orderfulfillment.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://openapi.etsy.com/v2/"

class EtsyServiceGenerator(
    private val context: Context,
    private val sessionManager: SessionManager
) {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val httpClient = OkHttpClient.Builder()

    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)

    private var retrofit = retrofitBuilder.build()

    fun <S> createService(serviceClass: Class<S>): S {
        val interceptor = AuthenticationInterceptor(sessionManager, BuildConfig.ETSY_CONSUMER_KEY)
        if (!httpClient.interceptors().contains(interceptor)) {
            httpClient.addInterceptor(interceptor)
            retrofitBuilder.client(httpClient.build())
            retrofit = retrofitBuilder.build()
        }
        return retrofit.create(serviceClass)
    }
}
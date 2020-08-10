package app.mblackman.orderfulfillment.data.network.etsy

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://openapi.etsy.com/v2/"

/**
 * Generates services to access the Etsy api.
 */
class EtsyServiceGenerator(sessionManager: SessionManager) {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val httpClient = OkHttpClient
        .Builder()
        .addInterceptor(
            EtsyApiInterceptor(
                sessionManager
            )
        )
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(httpClient)
        .baseUrl(BASE_URL)
        .build()

    /**
     * Create a service.
     *
     * @param serviceClass The class to generate the service from.
     * @return The new service.
     */
    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }
}
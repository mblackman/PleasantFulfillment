package app.mblackman.orderfulfillment.data.network.etsy

import app.mblackman.orderfulfillment.data.network.CredentialManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

private const val BASE_URL = "https://openapi.etsy.com/v2/"

/**
 * Generates services to access the Etsy api.
 */
class EtsyServiceGenerator @Inject constructor(credentialManager: CredentialManager) {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val httpClient = OkHttpClient
        .Builder()
        .addInterceptor(EtsyApiInterceptor(credentialManager))
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
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}
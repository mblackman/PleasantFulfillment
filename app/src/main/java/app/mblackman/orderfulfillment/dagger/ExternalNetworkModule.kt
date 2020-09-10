package app.mblackman.orderfulfillment.dagger

import app.mblackman.orderfulfillment.data.network.etsy.EtsyApiService
import app.mblackman.orderfulfillment.data.network.etsy.EtsyServiceGenerator
import app.mblackman.orderfulfillment.data.network.etsy.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ExternalNetworkModule {

    @Provides
    fun provideEtsyApiService(sessionManager: SessionManager): EtsyApiService =
        EtsyServiceGenerator(sessionManager).createService(EtsyApiService::class.java)
}
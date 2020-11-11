package app.mblackman.orderfulfillment.dagger

import app.mblackman.orderfulfillment.data.network.CredentialManager
import app.mblackman.orderfulfillment.data.network.etsy.EtsyApiService
import app.mblackman.orderfulfillment.data.network.etsy.EtsyServiceGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object ExternalNetworkModule {

    @Provides
    fun provideEtsyApiService(credentialManager: CredentialManager): EtsyApiService =
        EtsyServiceGenerator(credentialManager).createService(EtsyApiService::class.java)
}
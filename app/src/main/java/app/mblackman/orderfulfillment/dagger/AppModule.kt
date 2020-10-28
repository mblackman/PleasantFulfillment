package app.mblackman.orderfulfillment.dagger

import android.content.Context
import app.mblackman.orderfulfillment.BuildConfig
import app.mblackman.orderfulfillment.data.network.CredentialManager
import app.mblackman.orderfulfillment.data.network.CredentialManagerImpl
import app.mblackman.orderfulfillment.data.network.CredentialSource
import app.mblackman.orderfulfillment.data.network.etsy.Configuration
import app.mblackman.orderfulfillment.data.network.etsy.SharedPreferencesConfiguration
import app.mblackman.orderfulfillment.ui.login.OAuth1RedirectLogin
import app.mblackman.orderfulfillment.ui.login.RedirectLogin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager =
        CredentialManagerImpl(context)

    @Provides
    @Singleton
    fun provideConfiguration(@ApplicationContext context: Context): Configuration =
        SharedPreferencesConfiguration(context)

    @Provides
    fun provideEtsyRedirectLogin(credentialManager: CredentialManager): RedirectLogin =
        OAuth1RedirectLogin(
            BuildConfig.ETSY_CONSUMER_KEY,
            BuildConfig.ETSY_CONSUMER_SECRET,
            BuildConfig.ETSY_REDIRECT_URL,
            CredentialSource.Etsy,
            credentialManager
        )
}
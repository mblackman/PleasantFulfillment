package app.mblackman.orderfulfillment.dagger

import android.content.Context
import app.mblackman.orderfulfillment.BuildConfig
import app.mblackman.orderfulfillment.data.common.SafeItemStore
import app.mblackman.orderfulfillment.data.common.SafeItemStoreImpl
import app.mblackman.orderfulfillment.data.network.CredentialManager
import app.mblackman.orderfulfillment.data.network.CredentialManagerImpl
import app.mblackman.orderfulfillment.data.network.CredentialSource
import app.mblackman.orderfulfillment.data.network.StoreAdapter
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
    fun provideSafeItemStore(@ApplicationContext context: Context): SafeItemStore =
        SafeItemStoreImpl(context)

    @Provides
    @Singleton
    fun provideCredentialManager(safeItemStore: SafeItemStore): CredentialManager =
        CredentialManagerImpl(safeItemStore)

    @Provides
    fun provideEtsyRedirectLogin(credentialManager: CredentialManager): RedirectLogin =
        OAuth1RedirectLogin(
            BuildConfig.ETSY_CONSUMER_KEY,
            BuildConfig.ETSY_CONSUMER_SECRET,
            BuildConfig.ETSY_REDIRECT_URL,
            CredentialSource.Etsy,
            credentialManager
        )

    @Provides
    fun provideStoreAdapter(
        availableAdapters: Map<Int, @JvmSuppressWildcards StoreAdapter>
    ): StoreAdapter {
        return checkNotNull(availableAdapters[BuildConfig.STORE_ADAPTER_ID])
        { "No Store Adapter was provided matching id: ${BuildConfig.STORE_ADAPTER_ID}." }
    }
}
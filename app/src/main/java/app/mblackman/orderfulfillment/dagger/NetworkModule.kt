package app.mblackman.orderfulfillment.dagger

import app.mblackman.orderfulfillment.data.network.StoreAdapter
import app.mblackman.orderfulfillment.data.network.etsy.EtsyStoreAdapter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.multibindings.IntKey
import dagger.multibindings.IntoMap

@Module
@InstallIn(ApplicationComponent::class)
abstract class NetworkModule {

    @Binds
    @IntoMap
    @IntKey(EtsyStoreAdapter.AdapterId)
    abstract fun bindStoreAdapter(storeAdapter: EtsyStoreAdapter): StoreAdapter
}
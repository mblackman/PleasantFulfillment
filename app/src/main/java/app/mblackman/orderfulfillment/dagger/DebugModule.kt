package app.mblackman.orderfulfillment.dagger

import app.mblackman.orderfulfillment.data.network.StoreAdapter
import app.mblackman.orderfulfillment.ui.debug.MockStoreAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.multibindings.IntKey
import dagger.multibindings.IntoMap

@Module
@InstallIn(ApplicationComponent::class)
object DebugModule {
    @Provides
    @IntoMap
    @IntKey(MockStoreAdapter.MockAdapterId)
    fun provideStoreAdapter(): StoreAdapter = MockStoreAdapter()
}
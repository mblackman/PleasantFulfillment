package app.mblackman.orderfulfillment.dagger

import app.mblackman.orderfulfillment.data.repository.OrderRepository
import app.mblackman.orderfulfillment.data.repository.OrderRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository
}
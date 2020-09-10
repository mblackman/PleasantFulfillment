package app.mblackman.orderfulfillment.dagger

import android.content.Context
import app.mblackman.orderfulfillment.data.network.etsy.Configuration
import app.mblackman.orderfulfillment.data.network.etsy.SessionManager
import app.mblackman.orderfulfillment.data.network.etsy.SharedPreferencesConfiguration
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
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager =
        SessionManager(context)

    @Provides
    @Singleton
    fun provideConfiguration(@ApplicationContext context: Context): Configuration =
        SharedPreferencesConfiguration(context)
}
package app.mblackman.orderfulfillment.dagger

import android.content.Context
import androidx.room.Room
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideStoreDatabase(@ApplicationContext context: Context): StoreDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            StoreDatabase::class.java,
            "StoreDatabase"
        ).build()
}
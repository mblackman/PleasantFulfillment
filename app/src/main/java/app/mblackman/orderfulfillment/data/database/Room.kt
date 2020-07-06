package app.mblackman.orderfulfillment.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Dao interface to define methods to interact with room database.
 */
@Dao
interface StoreDao {

    /**
     * Gets all the order details from the dao.
     */
    @Query("select * from orderdetails")
    fun getOrderDetails(): LiveData<List<OrderDetails>>

    /**
     * Inserts all the given order details in the dao.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(orderDetails: List<OrderDetails>)
}

/**
 * The database with the dao.
 */
@Database(entities = [OrderDetails::class], version = 1, exportSchema = false)
abstract class StoreDatabase : RoomDatabase() {

    abstract val storeDao: StoreDao
}

private lateinit var INSTANCE: StoreDatabase

/**
 * Gets the database instance.
 *
 * @param context The context of the caller.
 * @return The database instance.
 */
fun getDatabase(context: Context): StoreDatabase {
    synchronized(StoreDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                StoreDatabase::class.java,
                "store"
            ).build()
        }
    }

    return INSTANCE
}
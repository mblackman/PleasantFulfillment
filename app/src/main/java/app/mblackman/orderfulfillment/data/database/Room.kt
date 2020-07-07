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
    @Query("SELECT * FROM orderdetails")
    fun getOrderDetails(): LiveData<List<OrderDetails>>

    /**
     * Inserts all the given order details in the dao.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(orderDetails: List<OrderDetails>)
}

/**
 * Dao interface to define methods to interact with room database.
 */
@Dao
interface UserDao {

    /**
     * Gets a user by id.
     *
     * @param id The id to look up the user with.
     * @return The user if found, else null.
     */
    @Query("SELECT * FROM user WHERE id = :id")
    fun findUserById(id: Int): User?
}

/**
 * The database containing all the data for the store and user.
 */
@Database(entities = [OrderDetails::class, User::class], version = 1, exportSchema = false)
abstract class StoreDatabase : RoomDatabase() {

    abstract val storeDao: StoreDao
    abstract val userDao: UserDao
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
                "StoreDatabase"
            ).build()
        }
    }

    return INSTANCE
}
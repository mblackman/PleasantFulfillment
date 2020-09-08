package app.mblackman.orderfulfillment.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines base members for [Dao]s.
 */
interface BaseDao<T> {
    /**
     * Inserts all the [T] into the dao.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<T>)
}

/**
 * [Dao] interface to define methods to interact with [OrderDetails] in a room database.
 */
@Dao
interface OrderDetailsDao : BaseDao<OrderDetails> {
    /**
     * Gets all the [OrderDetails] from the dao.
     *
     * @return A [LiveData] containing all the [OrderDetails]
     */
    @Query("SELECT * FROM order_details")
    fun getOrderDetails(): LiveData<List<OrderDetails>>

    /**
     * Gets [OrderDetails] with their sales data.
     */
    @Transaction
    @Query("SELECT * FROM order_details")
    fun getOrderDetailsWithProducts(): LiveData<List<OrderDetailsWithProductSales>>

    /**
     * Gets all the [OrderDetails] that match the given parameters.
     *
     * @param adapterId The id of the id to get [OrderDetails] from.
     * @return All the [OrderDetails] meeting the criteria.
     */
    @Query("SELECT * FROM order_details WHERE adapter_id=:adapterId")
    fun getOrderDetailsByAdapter(adapterId: Int): List<OrderDetails>
}

/**
 * [Dao] interface to define methods to interact with [Product] in a room database.
 */
@Dao
interface ProductDao : BaseDao<Product>

/**
 * [Dao] interface to define methods to interact with [ProductSale] in a room database.
 */
@Dao
interface ProductSaleDao : BaseDao<ProductSale> {
    /**
     * Gets all the [ProductSale] that match the given parameters.
     *
     * @param adapterId The id of the id to get [ProductSale] from.
     * @return All the [ProductSale] meeting the criteria.
     */
    @Query("SELECT * FROM product_sale WHERE adapter_id=:adapterId")
    fun getProductSalesByAdapter(adapterId: Int): List<ProductSale>
}

/**
 * The database containing all the data for the store and user.
 */
@Database(
    entities = [OrderDetails::class, Product::class, ProductSale::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class StoreDatabase : RoomDatabase() {
    /**
     * The [Dao] to interact with [OrderDetails].
     */
    abstract val orderDetailsDao: OrderDetailsDao

    /**
     * The [Dao] to interact with [Product]
     */
    abstract val productDao: ProductDao

    /**
     * The [Dao] to interact with [ProductSale]
     */
    abstract val productSaleDao: ProductSaleDao
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
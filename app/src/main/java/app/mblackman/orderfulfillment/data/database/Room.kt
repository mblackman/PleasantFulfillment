package app.mblackman.orderfulfillment.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

interface BaseDao<T> {
    /**
     * Inserts all the items into the dao.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(orderDetails: List<T>)
}

/**
 * Dao interface to define methods to interact with room database.
 */
@Dao
interface StoreDao : BaseDao<OrderDetails> {

    /**
     * Gets all the order details from the dao.
     */
    @Query("SELECT * FROM order_details")
    fun getOrderDetails(): LiveData<List<OrderDetails>>
}

@Dao
interface ProductDao : BaseDao<Product>

@Dao
interface ProductSaleDao : BaseDao<ProductSale> {
    @Query("SELECT * FROM product_sale LEFT JOIN product ON product_sale.product_sale_adapterId=product.product_adapterId AND product_sale.productId WHERE adapterId = :adapterId AND order_details_foreignKey = :orderId")
    fun getProductSalesWithProduct(adapterId: Int, orderId: Long): List<ProductSale>
}

/**
 * The database containing all the data for the store and user.
 */
@Database(
    entities = [OrderDetails::class, Product::class, ProductSale::class, EntityIdentity::class],
    version = 1,
    exportSchema = false
)
abstract class StoreDatabase : RoomDatabase() {

    abstract val storeDao: StoreDao
    abstract val productDao: ProductDao
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
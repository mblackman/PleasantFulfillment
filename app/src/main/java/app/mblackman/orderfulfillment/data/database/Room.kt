package app.mblackman.orderfulfillment.data.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
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
    fun getOrderDetailsWithProducts(): DataSource.Factory<Int, OrderDetailsWithProductSales>

    /**
     * Gets all the [OrderDetails] that match the given parameters.
     *
     * @param adapterId The id of the id to get [OrderDetails] from.
     * @return All the [OrderDetails] meeting the criteria.
     */
    @Query("SELECT * FROM order_details WHERE adapter_id=:adapterId")
    fun getOrderDetailsByAdapter(adapterId: Int): List<OrderDetails>

    @Query("SELECT order_details_id FROM order_details WHERE adapter_id=:adapterId AND adapter_entity_key=:adapterEntityKey LIMIT 1")
    fun getOrderDetailsId(adapterId: Int, adapterEntityKey: Long): Long?
}

/**
 * [Dao] interface to define methods to interact with [Product] in a room database.
 */
@Dao
interface ProductDao : BaseDao<Product> {
    @Query("SELECT product_id FROM product WHERE adapter_id=:adapterId AND adapter_entity_key=:adapterEntityKey LIMIT 1")
    fun getProductId(adapterId: Int, adapterEntityKey: Long): Long?

    /**
     * Gets all the [OrderDetails] that match the given parameters.
     *
     * @param adapterId The id of the id to get [OrderDetails] from.
     * @return All the [OrderDetails] meeting the criteria.
     */
    @Query("SELECT * FROM product WHERE adapter_id=:adapterId")
    fun getProductByAdapter(adapterId: Int): List<Product>
}

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

package app.mblackman.orderfulfillment.data.repository

import android.content.Context
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.database.getDatabase
import app.mblackman.orderfulfillment.data.network.StoreAdapter
import app.mblackman.orderfulfillment.data.network.etsy.EtsyStoreAdapter

/**
 * Implementation of [ProductRepository] that interacts with the [StoreAdapter] and
 * [StoreDatabase].
 */
class ProductRepositoryImpl(
    private val storeAdapter: StoreAdapter,
    private val storeDatabase: StoreDatabase
) : ProductRepository() {

    private val productSaleConverter = ProductSaleConverter(storeAdapter.adapterId)

    companion object {
        /**
         * Creates a [ProductRepositoryImpl] from the given [context].
         *
         * @param  context The context to create the [ProductRepositoryImpl] from.
         * @return The created [ProductRepositoryImpl].
         */
        fun create(context: Context): ProductRepositoryImpl {
            return ProductRepositoryImpl(
                EtsyStoreAdapter.create(context),
                getDatabase(context)
            )
        }
    }

    /**
     * Gets the latest ProductSales and inserts them to the database.
     */
    override suspend fun updateProductSales() {
        getAndUpdate(
            storeAdapter::getProductSales,
            { storeDatabase.productSaleDao.getProductSalesByAdapter(storeAdapter.adapterId) },
            { results -> storeDatabase.productSaleDao.insertAll(results) },
            productSaleConverter
        )
    }

}
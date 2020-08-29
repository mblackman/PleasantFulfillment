package app.mblackman.orderfulfillment.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.database.asMap
import app.mblackman.orderfulfillment.data.database.getDatabase
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.network.StoreAdapter
import app.mblackman.orderfulfillment.data.network.etsy.EtsyStoreAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of OrderRepository that fetches order details from the web
 * and persists them in a database.
 */
class OrderRepositoryImpl(
    private val storeAdapter: StoreAdapter,
    private val storeDatabase: StoreDatabase
) : OrderRepository() {

    companion object {
        /**
         * Creates a [OrderRepository] from the given [context].
         *
         * @param  context The context to create the [OrderRepository] from.
         * @return The created [OrderRepository].
         */
        fun create(context: Context): OrderRepository {
            return OrderRepositoryImpl(
                EtsyStoreAdapter.create(context),
                getDatabase(context)
            )
        }
    }

    /**
     * Gets the list of orders.
     */
    override val orderDetails: LiveData<List<Order>> =
        Transformations.map(storeDatabase.storeDao.getOrderDetails()) {
            it.map { orderDetails ->
                Order(
                    orderDetails.orderDetailsId.foreignKey,
                    "Order ${orderDetails.orderDetailsId.foreignKey} from Adapter ${storeAdapter.adapterId}",
                    orderDetails.status,
                    orderDetails.orderDate,
                    orderDetails.buyerName,
                    orderDetails.buyerEmail,
                    orderDetails.address,
                    storeDatabase.productSaleDao.getProductSalesWithProduct(
                        storeAdapter.adapterId,
                        orderDetails.orderDetailsId.foreignKey
                    ).map {
                        it.
                    },
                    orderDetails.properties?.asMap()
                )
            }
        }

    /**
     * Gets the latest order detail data and stores it.
     */
    override suspend fun updateOrderDetails() {

        storeAdapter.getProducts().let {
            withContext(Dispatchers.IO) {
                storeDatabase.productDao.insertAll(it.map { networkProduct ->
                    networkProduct.asDatabaseObject(storeAdapter.adapterId)
                })
            }
        }

        storeAdapter.getProductSales().let {
            withContext(Dispatchers.IO) {
                storeDatabase.productSaleDao.insertAll(it.map { networkProductSale ->
                    networkProductSale.asDatabaseObject(storeAdapter.adapterId)
                })
            }
        }

        storeAdapter.getOrders().let {
            withContext(Dispatchers.IO) {
                storeDatabase.storeDao.insertAll(it.map { networkOrder ->
                    networkOrder.asDatabaseObject(storeAdapter.adapterId)
                })
            }
        }
    }
}
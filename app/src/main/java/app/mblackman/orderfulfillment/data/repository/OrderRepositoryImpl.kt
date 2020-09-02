package app.mblackman.orderfulfillment.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.database.getDatabase
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.network.StoreAdapter
import app.mblackman.orderfulfillment.data.network.etsy.EtsyStoreAdapter

/**
 * Implementation of OrderRepository that fetches order details from the web
 * and persists them in a database.
 */
class OrderRepositoryImpl(
    private val storeAdapter: StoreAdapter,
    private val storeDatabase: StoreDatabase
) : OrderRepository() {

    private val orderEntityConverter = OrderEntityConverter(storeAdapter.adapterId)

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
        Transformations.map(storeDatabase.orderDetailsDao.getOrderDetails()) {
            it.map { orderDetails ->
                orderDetails.asDomainObject()
            }
        }

    /**
     * Gets the latest order detail data and stores it.
     */
    override suspend fun updateOrderDetails() {
        getAndUpdate(
            storeAdapter::getOrders,
            { storeDatabase.orderDetailsDao.getOrderDetailsByAdapter(storeAdapter.adapterId) },
            { results -> storeDatabase.orderDetailsDao.insertAll(results) },
            orderEntityConverter
        )
    }

}
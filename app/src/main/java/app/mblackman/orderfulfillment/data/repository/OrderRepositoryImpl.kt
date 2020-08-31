package app.mblackman.orderfulfillment.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.database.getDatabase
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.network.NetworkOrder
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
) : OrderRepository {

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
        storeAdapter.getOrders().let {
            withContext(Dispatchers.IO) {
                val existingMappings =
                    storeDatabase.orderDetailsDao.getOrderDetailsByAdapter(storeAdapter.adapterId)
                        .associateBy(OrderDetails::adapterEntityKey)

                storeDatabase.orderDetailsDao.insertAll(it.map { networkOrder ->
                    if (existingMappings.containsKey(networkOrder.id)) {
                        existingMappings.getValue(networkOrder.id).update(networkOrder)
                    } else {
                        networkOrder.asDatabaseObject(storeAdapter.adapterId)
                    }
                })
            }
        }
    }

    private fun OrderDetails.update(order: NetworkOrder) =
        this.copy(
            orderDate = order.orderDate.toDate(),
            buyerName = order.buyerName,
            buyerEmail = order.buyerEmail,
            address = order.address,
        )
}
package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData
import app.mblackman.orderfulfillment.data.database.OrderDetails

/**
 * Represents a repository of orders.
 */
abstract class OrderRepository : BaseRepository() {

    /**
     * Gets the live data collection of the order details.
     */
    abstract suspend fun getOrderDetails(): LiveData<List<OrderDetails>>
}
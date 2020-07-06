package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData
import app.mblackman.orderfulfillment.data.domain.Order

/**
 * Represents a repository of orders.
 */
abstract class OrderRepository : BaseRepository() {

    /**
     * Gets the live data collection of the order details.
     *
     * @return A live data with the collection of order details.
     */
    abstract suspend fun getOrderDetails(): LiveData<List<Order>>
}
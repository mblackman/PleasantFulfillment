package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.domain.Shop

/**
 * Represents a repository of orders.
 */
abstract class OrderRepository : BaseRepository() {

    /**
     * Gets the live data collection of the order details.
     *
     * @param shop The shop to get order details from.
     * @return A live data with the collection of order details.
     */
    abstract suspend fun getOrderDetails(shop: Shop): LiveData<List<Order>>
}
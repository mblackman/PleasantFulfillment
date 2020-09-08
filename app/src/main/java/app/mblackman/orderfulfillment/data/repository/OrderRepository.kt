package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import app.mblackman.orderfulfillment.data.domain.Order

/**
 * Represents a repository of orders.
 */
abstract class OrderRepository : BaseRepository() {

    /**
     * Gets the list of orders.
     */
    abstract val orderDetails: LiveData<PagedList<Order>>

    /**
     * Gets the latest order detail data and stores it.
     */
    abstract suspend fun updateOrderDetails()
}
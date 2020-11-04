package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import app.mblackman.orderfulfillment.data.domain.Order

/**
 * Represents a repository of orders.
 */
interface OrderRepository : BaseRepository {

    /**
     * Gets the list of orders.
     */
    val orderDetails: LiveData<PagedList<Order>>

    /**
     * Gets the latest order detail data and stores it.
     */
    suspend fun updateOrderDetails()
}
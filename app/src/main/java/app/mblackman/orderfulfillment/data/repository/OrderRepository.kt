package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData
import app.mblackman.orderfulfillment.data.domain.Order

/**
 * Represents a repository of orders.
 */
interface OrderRepository {

    /**
     * Gets the list of orders.
     */
    val orderDetails: LiveData<List<Order>>

    /**
     * Gets the latest order detail data and stores it.
     */
    suspend fun updateOrderDetails()
}
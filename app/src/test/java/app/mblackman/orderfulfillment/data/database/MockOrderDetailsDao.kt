package app.mblackman.orderfulfillment.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

/**
 * A mock implementation of [OrderDetailsDao]
 */
class MockOrderDetailsDao(orderDetails: List<OrderDetails>? = null) : OrderDetailsDao {
    private val orderDetailsMap = mutableMapOf<ItemKey, OrderDetails>()
    private val liveOrderDetails = MutableLiveData<List<OrderDetails>>()

    init {
        updateOrderDetails(orderDetails)
    }

    override fun getOrderDetails(): LiveData<List<OrderDetails>> = liveOrderDetails

    /**
     * Gets [OrderDetails] with their sales data.
     */
    override fun getOrderDetailsWithProducts(): DataSource.Factory<Int, OrderDetailsWithProductSales> {
        TODO("Not yet implemented")
    }

    override fun getOrderDetailsByAdapter(adapterId: Int): List<OrderDetails> =
        orderDetailsMap.filter { it.key.adapterId == adapterId }.values.toList()

    override fun getOrderDetailsId(adapterId: Int, adapterEntityKey: Long): Long? {
        TODO("Not yet implemented")
    }

    override fun insertAll(items: List<OrderDetails>) {
        updateOrderDetails(items)
    }

    private fun updateOrderDetails(orderDetails: List<OrderDetails>?) {
        orderDetails?.forEach {
            orderDetailsMap[ItemKey(it.adapterId, it.adapterEntityKey)] = it
        }
        liveOrderDetails.postValue(orderDetailsMap.values.toList())
    }

    data class ItemKey(val adapterId: Int, val entityKey: Long)
}
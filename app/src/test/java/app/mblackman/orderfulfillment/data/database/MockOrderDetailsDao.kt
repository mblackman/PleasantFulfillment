package app.mblackman.orderfulfillment.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MockOrderDetailsDao(private var orderDetails: List<OrderDetails>) : OrderDetailsDao {
    private val liveOrderDetails = MutableLiveData<List<OrderDetails>>()

    init {
        liveOrderDetails.value = orderDetails
    }

    override fun getOrderDetails(): LiveData<List<OrderDetails>> = liveOrderDetails

    override fun getOrderDetailsByAdapter(adapterId: Int): List<OrderDetails> =
        liveOrderDetails.value ?: emptyList()

    override fun insertAll(items: List<OrderDetails>) {
        this.orderDetails = this.orderDetails + items
        this.liveOrderDetails.value = this.orderDetails
    }

}
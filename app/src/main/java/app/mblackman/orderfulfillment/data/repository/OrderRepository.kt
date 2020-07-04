package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.domain.OrderDetails
import kotlinx.coroutines.Deferred

abstract class OrderRepository {
    abstract suspend fun getOrderDetails(): Deferred<List<OrderDetails>>
}
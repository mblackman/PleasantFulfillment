package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.domain.OrderDetails

abstract class OrderRepository {
    abstract suspend fun getOrderDetails(): List<OrderDetails>?
}
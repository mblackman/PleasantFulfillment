package app.mblackman.orderfulfillment.data.network

import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import java.time.LocalDate

/**
 * Represents a product for sale.
 */
data class NetworkProduct(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Double?
)

/**
 * Represents a sale of a product.
 */
data class NetworkProductSale(
    val id: Long,
    val productId: Long,
    val orderId: Long,
    val quantity: Int
)

/**
 * Represents an order.
 */
data class NetworkOrder(
    val id: Long,
    val status: OrderStatus,
    val orderDate: LocalDate,
    val buyerName: String,
    val buyerEmail: String,
    val address: Address,
    val properties: Map<String, String>?
)
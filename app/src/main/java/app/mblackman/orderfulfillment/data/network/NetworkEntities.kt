package app.mblackman.orderfulfillment.data.network

import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import java.time.LocalDateTime

/**
 * The base data for an item from the net.
 */
interface NetworkItem {
    /**
     * The id of the item from the network source.
     */
    val id: Long
}

/**
 * Represents a product for sale.
 */
data class NetworkProduct(
    override val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Double?
) : NetworkItem

/**
 * Represents a sale of a product.
 */
data class NetworkProductSale(
    override val id: Long,
    val productId: Long,
    val orderId: Long,
    val quantity: Int
) : NetworkItem

/**
 * Represents an order.
 */
data class NetworkOrder(
    override val id: Long,
    val status: OrderStatus,
    val orderDate: LocalDateTime,
    val buyerName: String,
    val buyerEmail: String,
    val address: Address,
    val properties: Map<String, String>?
) : NetworkItem
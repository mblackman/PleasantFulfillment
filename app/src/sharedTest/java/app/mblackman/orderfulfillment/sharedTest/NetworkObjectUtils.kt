package app.mblackman.orderfulfillment.sharedTest

import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import app.mblackman.orderfulfillment.data.network.NetworkOrder
import app.mblackman.orderfulfillment.data.network.NetworkProduct
import app.mblackman.orderfulfillment.data.network.NetworkProductSale
import app.mblackman.orderfulfillment.data.util.toLocalDateTime
import java.time.LocalDateTime

/**
 * Utilities to help create Network objects.
 */
class NetworkObjectUtils {
    companion object {
        /**
         * Creates a network order with either given values or defaults.
         */
        fun createOrder(
            id: Long = DefaultLongId,
            orderStatus: OrderStatus = DefaultStatus,
            localDateTime: LocalDateTime = DefaultOrderDate.toLocalDateTime(),
            buyerName: String = DefaultBuyerName,
            buyerEmail: String = DefaultBuyerEmail,
            address: Address = DefaultAddress,
            properties: Map<String, String>? = null
        ) =
            NetworkOrder(
                id,
                orderStatus,
                localDateTime,
                buyerName,
                buyerEmail,
                address,
                properties
            )

        fun createProduct(
            id: Long = DefaultLongId,
            name: String = DefaultProductName,
            description: String = DefaultProductDescription,
            imageUrl: String? = DefaultProductImageUrl,
            price: Double? = DefaultPrice
        ) =
            NetworkProduct(
                id,
                name,
                description,
                imageUrl,
                price
            )

        fun createProductSale(
            id: Long = DefaultLongId,
            productId: Long = DefaultLongId,
            orderId: Long = DefaultLongId,
            quantity: Int = DefaultQuantity
        ) = NetworkProductSale(id, productId, orderId, quantity)
    }
}
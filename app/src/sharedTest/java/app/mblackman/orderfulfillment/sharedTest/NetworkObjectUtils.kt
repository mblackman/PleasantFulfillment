package app.mblackman.orderfulfillment.sharedTest

import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import app.mblackman.orderfulfillment.data.network.NetworkOrder
import java.time.LocalDateTime

/**
 * Utilities to help create Network objects.
 */
class NetworkObjectUtils {
    companion object {
        /**
         * Creates a network order with either given values or defaults.
         */
        fun createNetworkOrder(
            id: Long = 0,
            orderStatus: OrderStatus = OrderStatus.Purchased,
            localDateTime: LocalDateTime = LocalDateTime.now(),
            buyerName: String = "Test Buyer Name",
            buyerEmail: String = "Test@Email.com",
            address: Address = Address(
                "Test Name",
                "First Line",
                "Second Line",
                "Test City",
                "Test State",
                "Test Zip"
            ),
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
    }
}
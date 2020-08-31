package app.mblackman.orderfulfillment.sharedTest

import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import app.mblackman.orderfulfillment.data.database.OrderDetails
import java.time.Instant
import java.util.*

/**
 * Utilities to assist with database tests.
 */
class DomainObjectUtils {
    companion object {
        const val defaultAdapterId: Int = 1

        /**
         * Creates a [OrderDetails] with some defaults already provided.
         */
        fun createOrderDetails(
            orderDetailsId: Long = 0,
            adapterId: Int = defaultAdapterId,
            adapterEntityKey: Long = 0,
            status: OrderStatus = OrderStatus.Purchased,
            orderDate: Date = Date.from(Instant.now()),
            buyerName: String = "Test User Name",
            buyerEmail: String = "TestBuyer@email.com",
            address: Address = Address(
                "Test User Name",
                "Test First Line",
                "Test Second Line",
                "Test City",
                "Test State",
                "Test Zip"
            )
        ): OrderDetails =
            OrderDetails(
                orderDetailsId,
                adapterId,
                adapterEntityKey,
                status,
                orderDate,
                buyerName,
                buyerEmail,
                address
            )
    }

}
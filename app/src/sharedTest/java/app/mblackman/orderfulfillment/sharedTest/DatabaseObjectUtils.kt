package app.mblackman.orderfulfillment.sharedTest

import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import app.mblackman.orderfulfillment.data.database.OrderDetails
import java.util.*

/**
 * Utilities to assist with database tests.
 */
class DatabaseObjectUtils {
    companion object {
        /**
         * Creates a [OrderDetails] with some defaults already provided.
         */
        fun createOrderDetails(
            orderDetailsId: Long = DefaultLongId,
            adapterId: Int = DefaultAdapterId,
            adapterEntityKey: Long = DefaultAdapterEntityKey,
            status: OrderStatus = DefaultStatus,
            orderDate: Date = DefaultOrderDate,
            buyerName: String = DefaultBuyerName,
            buyerEmail: String = DefaultBuyerEmail,
            address: Address = DefaultAddress
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
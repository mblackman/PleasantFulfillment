package app.mblackman.orderfulfillment.sharedTest

import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.Product
import app.mblackman.orderfulfillment.data.database.ProductSale
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

        fun createProductSale(
            id: Long = DefaultLongId,
            adapterId: Int = DefaultAdapterId,
            adapterEntityKey: Long = DefaultAdapterEntityKey,
            orderDetailsId: Long = DefaultLongId,
            productId: Long = DefaultLongId,
            quantity: Int = DefaultProductSaleQuantity
        ): ProductSale =
            ProductSale(
                id,
                adapterId,
                adapterEntityKey,
                orderDetailsId,
                productId,
                quantity
            )

        fun createProduct(
            id: Long = DefaultLongId,
            adapterId: Int = DefaultAdapterId,
            adapterEntityKey: Long = DefaultAdapterEntityKey,
            name: String = DefaultProductName,
            description: String = DefaultProductDescription,
            imageUrl: String? = null,
            price: Double? = DefaultPrice
        ): Product =
            Product(
                id,
                adapterId,
                adapterEntityKey,
                name,
                description,
                imageUrl,
                price
            )
    }

}
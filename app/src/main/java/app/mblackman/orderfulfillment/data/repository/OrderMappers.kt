package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.common.OrderStatus
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.domain.Address
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.domain.Product
import app.mblackman.orderfulfillment.data.domain.ProductSale
import app.mblackman.orderfulfillment.data.network.etsy.json.Receipt
import java.time.LocalDate

/**
 * Maps a receipt object to order details.
 */
class ReceiptToOrderMapper : Mapper<Receipt, OrderDetails> {

    /**
     * Maps the input object to the expected output type.
     *
     * @param input The input to map.
     * @return The mapped object based on data from the input.
     */
    override fun map(input: Receipt): OrderDetails {
        return OrderDetails(input.id)
    }
}

/**
 * Maps a receipt object to order details.
 */
class OrderDetailsToOrderMapper : Mapper<OrderDetails, Order> {

    /**
     * Maps the input object to the expected output type.
     *
     * @param input The input to map.
     * @return The mapped object based on data from the input.
     */
    override fun map(input: OrderDetails): Order {
        return Order(
            input.id,
            "Temp",
            OrderStatus.Purchased,
            LocalDate.now(),
            "Temp",
            "Temp",
            Address("Temp", "Temp", "Temp", "Temp", "Temp", "Temp"),
            listOf(ProductSale(1, Product("Product 3", "Product 3 descriptor", "null", 17.95), 1))
        )
    }
}
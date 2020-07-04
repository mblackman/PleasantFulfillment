package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.domain.OrderDetails
import app.mblackman.orderfulfillment.data.network.Receipt

/**
 * Maps a receipt object to order details.
 */
class ReceiptToOrderMapper : Mapper<Receipt, OrderDetails> {
    override fun map(input: Receipt): OrderDetails {
        return OrderDetails(input.id)
    }
}
package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.network.Receipt

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
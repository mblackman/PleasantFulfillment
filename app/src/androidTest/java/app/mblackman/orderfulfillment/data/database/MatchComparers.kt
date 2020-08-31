package app.mblackman.orderfulfillment.data.database

/**
 * A function to compare [OrderDetails] and ignore the id assigned by the database.
 *
 * @param expected The expected [OrderDetails].
 * @param result The result from a test.
 * @return True if the items are equivalent, else false.
 */
fun orderDetailsCompare(expected: OrderDetails?, result: Any?): Boolean {
    if (expected == null) {
        return result == null
    }

    if (result !is OrderDetails) {
        return false
    }

    return expected.adapterEntityKey == result.adapterEntityKey
            && expected.adapterId == result.adapterId
            && expected.address == result.address
            && expected.buyerEmail == result.buyerEmail
            && expected.buyerName == result.buyerName
            && expected.orderDate == result.orderDate
            && expected.status == result.status
}
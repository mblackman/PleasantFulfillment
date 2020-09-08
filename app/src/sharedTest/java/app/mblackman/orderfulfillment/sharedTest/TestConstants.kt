package app.mblackman.orderfulfillment.sharedTest

import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import java.time.Instant
import java.util.*

const val DefaultLongId: Long = 0
const val DefaultAdapterId: Int = -1
const val DefaultAdapterEntityKey: Long = 0
const val DefaultProductSaleQuantity: Int = 1
const val DefaultProductName = "Default Product"
const val DefaultProductDescription = "Default Product Described"
const val DefaultPrice: Double = 1.00
val DefaultStatus: OrderStatus = OrderStatus.Purchased
val DefaultOrderDate: Date = Date.from(Instant.ofEpochMilli(100))
const val DefaultBuyerName: String = "Test User Name"
const val DefaultBuyerEmail: String = "TestBuyer@email.com"
val DefaultAddress: Address = Address(
    "Test User Name",
    "Test First Line",
    "Test Second Line",
    "Test City",
    "Test State",
    "Test Zip"
)
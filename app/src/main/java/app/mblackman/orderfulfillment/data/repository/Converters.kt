package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.database.DefaultPrimaryKey
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.Product
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.network.NetworkOrder
import app.mblackman.orderfulfillment.data.network.NetworkProduct
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

/**
 * Converts a [Date] to [LocalDateTime]
 */
fun Date.toLocalDateTime(): LocalDateTime =
    Instant.ofEpochMilli(this.time).atZone(ZoneId.systemDefault()).toLocalDateTime()

/**
 * Converts a [LocalDateTime] to [Date]
 */
fun LocalDateTime.toDate(): Date =
    Date.from(this.atZone(ZoneId.systemDefault()).toInstant())

/**
 * Converts the database [OrderDetails] object to an [Order]
 */
fun OrderDetails.asDomainObject(): Order =
    Order(
        this.orderDetailsId,
        "Order ${this.orderDetailsId}.",
        this.status,
        this.orderDate.toLocalDateTime(),
        this.buyerName,
        this.buyerEmail,
        this.address,
        null
    )

/**
 * Converts a [NetworkOrder] to a database [OrderDetails].
 *
 * @param adapterId The id of the StoreAdapter this [NetworkOrder] came from
 */
fun NetworkOrder.asDatabaseObject(adapterId: Int): OrderDetails =
    OrderDetails(
        DefaultPrimaryKey,
        adapterId,
        this.id,
        this.status,
        this.orderDate.toDate(),
        this.buyerName,
        this.buyerEmail,
        this.address
    ).apply {
        this.adapterId
    }

/**
 * Converts a [NetworkProduct] to a database [Product].
 *
 * @param primaryKey The primary key of an existing entry if it exists. Send null if no entry exists.
 */
fun NetworkProduct.asDatabaseObject(primaryKey: Long?): Product =
    Product(
        primaryKey ?: DefaultPrimaryKey,
        this.name,
        this.description,
        this.imageUrl,
        this.price
    )
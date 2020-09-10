package app.mblackman.orderfulfillment.data.util

import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.OrderDetailsWithProductSales
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.domain.Product
import app.mblackman.orderfulfillment.data.domain.ProductSale
import app.mblackman.orderfulfillment.data.network.NetworkOrder
import app.mblackman.orderfulfillment.data.network.NetworkProduct
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import app.mblackman.orderfulfillment.data.database.Product as DbProduct

const val DefaultPrimaryKey: Long = 0

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
 * Converts the data [OrderDetailsWithProductSales] to the domain [Order].
 */
fun OrderDetailsWithProductSales.asDomainObject(): Order =
    Order(
        this.orderDetails.localId,
        "Order ${this.orderDetails.localId}.",
        this.orderDetails.status,
        this.orderDetails.orderDate.toLocalDateTime(),
        this.orderDetails.buyerName,
        this.orderDetails.buyerEmail,
        this.orderDetails.address,
        null,
        this.productSales.map {
            ProductSale(
                it.productSale.localId,
                Product(
                    it.product.name,
                    it.product.description,
                    it.product.imageUrl,
                    it.product.price
                ),
                it.productSale.quantity
            )
        }
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
    )

/**
 * Converts the [NetworkProduct] to the database [DbProduct]
 */
fun NetworkProduct.asDatabaseObject(adapterId: Int): DbProduct =
    DbProduct(
        DefaultPrimaryKey,
        adapterId,
        this.id,
        this.name,
        this.description,
        this.imageUrl,
        this.price
    )
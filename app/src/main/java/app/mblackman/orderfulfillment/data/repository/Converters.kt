package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.database.DefaultPrimaryKey
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.Product
import app.mblackman.orderfulfillment.data.database.ProductSale
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.network.NetworkOrder
import app.mblackman.orderfulfillment.data.network.NetworkProduct
import app.mblackman.orderfulfillment.data.network.NetworkProductSale
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
        this.localId,
        "Order ${this.localId}.",
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
 * @param adapterId The id of the adapter the [NetworkProduct] came from.
 */
fun NetworkProduct.asDatabaseObject(adapterId: Int): Product =
    Product(
        DefaultPrimaryKey,
        adapterId,
        this.id,
        this.name,
        this.description,
        this.imageUrl,
        this.price
    )

fun NetworkProductSale.asDatabaseObject(adapterId: Int): ProductSale =
    ProductSale(
        DefaultPrimaryKey,
        adapterId,
        this.id,
        this.orderId,
        this.productId,
        this.quantity
    )

class OrderEntityConverter(private val adapterId: Int) :
    EntityConverter<OrderDetails, NetworkOrder> {
    override val canUpdateExisting: Boolean = true

    override fun toEntity(item: NetworkOrder): OrderDetails = item.asDatabaseObject(adapterId)

    override fun updateExisting(existingEntity: OrderDetails, item: NetworkOrder): OrderDetails {
        return existingEntity.copy(
            orderDate = item.orderDate.toDate(),
            buyerName = item.buyerName,
            buyerEmail = item.buyerEmail,
            address = item.address,
        )
    }

}

class ProductSaleConverter(private val adapterId: Int) :
    EntityConverter<ProductSale, NetworkProductSale> {
    override val canUpdateExisting: Boolean = true

    override fun toEntity(item: NetworkProductSale): ProductSale = item.asDatabaseObject(adapterId)

    override fun updateExisting(
        existingEntity: ProductSale,
        item: NetworkProductSale
    ): ProductSale = item.asDatabaseObject(adapterId)

}
package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.database.*
import app.mblackman.orderfulfillment.data.network.NetworkOrder
import app.mblackman.orderfulfillment.data.network.NetworkProduct
import app.mblackman.orderfulfillment.data.network.NetworkProductSale

const val DefaultPrimaryKey: Long = 0

fun NetworkOrder.asDatabaseObject(adapterId: Int): OrderDetails =
    OrderDetails(
        EntityIdentity(adapterId, this.id),
        this.status,
        this.orderDate,
        this.buyerName,
        this.buyerEmail,
        this.address,
        this.properties?.asProperties()
    )

fun NetworkProductSale.asDatabaseObject(adapterId: Int): ProductSale =
    ProductSale(
        EntityIdentity(adapterId, this.id),
        EntityIdentity(adapterId, this.orderId),
        EntityIdentity(adapterId, this.productId),
        this.quantity
    )

fun NetworkProduct.asDatabaseObject(adapterId: Int): Product =
    Product(
        EntityIdentity(adapterId, this.id),
        this.name,
        this.description,
        this.imageUrl,
        this.price
    )
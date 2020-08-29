package app.mblackman.orderfulfillment.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import java.time.LocalDate

data class EntityIdentity(
    val adapterId: Int,
    val foreignKey: Long
)

/**
 * Represents a product for sale.
 */
@Entity
data class Product(
    @PrimaryKey
    @Embedded(prefix = "product_")
    val productId: EntityIdentity,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Double?
)

/**
 * Represents a sale of a product.
 */
@Entity(tableName = "product_sale")
data class ProductSale(
    @PrimaryKey
    @Embedded(prefix = "product_sale_")
    val productSaleId: EntityIdentity,
    val orderDetailId: Long,
    val productId: Long,
    val quantity: Int
)

data class OrderAndProductSaleDetails(
    @Embedded
    val productSales: ProductSale,
    @Embedded
    val product: Product
)

/**
 * Represents an order.
 */
@Entity(tableName = "order_details")
data class OrderDetails(
    @PrimaryKey
    @Embedded(prefix = "order_details_")
    val orderDetailsId: EntityIdentity,
    val status: OrderStatus,
    val orderDate: LocalDate,
    val buyerName: String,
    val buyerEmail: String,
    @Embedded
    val address: Address,
    @Embedded
    val properties: List<Property>?
)
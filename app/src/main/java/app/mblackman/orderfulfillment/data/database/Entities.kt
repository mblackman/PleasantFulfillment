package app.mblackman.orderfulfillment.data.database

import androidx.room.*
import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import java.util.*

const val DefaultPrimaryKey: Long = 0

/**
 * Represents a product for sale.
 */
@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    val productId: Long,
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
    @PrimaryKey(autoGenerate = true)
    val productSaleId: Long,
    val orderDetailId: Long,
    val productId: Long,
    val quantity: Int
)

/**
 * Represents an order.
 */
@Entity(
    tableName = "order_details",
    indices = [Index(value = ["adapter_id", "adapter_entity_key"], unique = true)]
)
data class OrderDetails(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_details_id")
    val orderDetailsId: Long,

    @ColumnInfo(name = "adapter_id")
    val adapterId: Int,

    @ColumnInfo(name = "adapter_entity_key")
    val adapterEntityKey: Long,

    val status: OrderStatus,

    @ColumnInfo(name = "order_date")
    val orderDate: Date,

    @ColumnInfo(name = "buyer_name")
    val buyerName: String,

    @ColumnInfo(name = "buyer_email")
    val buyerEmail: String,

    @Embedded
    val address: Address
)

data class OrderDetailsProperty(
    @ColumnInfo(name = "order_details_id")
    val orderDetailsId: Long,
    override val key: String,
    override val value: String
) : Property()
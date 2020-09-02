package app.mblackman.orderfulfillment.data.database

import androidx.room.*
import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import java.util.*

const val DefaultPrimaryKey: Long = 0

/**
 * Defines an Entity that comes from a foreign data source.
 */
@Entity
interface AdapterEntity {
    /**
     * The id of the item in the local database.
     */
    val localId: Long

    /**
     * The id of the adapter the entity came from.
     */
    val adapterId: Int

    /**
     * The id used by the foreign adapter for this entity.
     */
    val adapterEntityKey: Long
}

/**
 * Represents a product for sale.
 */
@Entity(indices = [Index(value = ["adapter_id", "adapter_entity_key"], unique = true)])
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    override val localId: Long,

    @ColumnInfo(name = "adapter_id")
    override val adapterId: Int,

    @ColumnInfo(name = "adapter_entity_key")
    override val adapterEntityKey: Long,

    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Double?
) : AdapterEntity

/**
 * Represents a sale of a product.
 */
@Entity(
    tableName = "product_sale",
    indices = [Index(value = ["adapter_id", "adapter_entity_key"], unique = true)]
)
data class ProductSale(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_sale_id")
    override val localId: Long,

    @ColumnInfo(name = "adapter_id")
    override val adapterId: Int,

    @ColumnInfo(name = "adapter_entity_key")
    override val adapterEntityKey: Long,

    val orderDetailId: Long,
    val productId: Long,
    val quantity: Int
) : AdapterEntity

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
    override val localId: Long,

    @ColumnInfo(name = "adapter_id")
    override val adapterId: Int,

    @ColumnInfo(name = "adapter_entity_key")
    override val adapterEntityKey: Long,

    val status: OrderStatus,

    @ColumnInfo(name = "order_date")
    val orderDate: Date,

    @ColumnInfo(name = "buyer_name")
    val buyerName: String,

    @ColumnInfo(name = "buyer_email")
    val buyerEmail: String,

    @Embedded
    val address: Address
) : AdapterEntity

data class OrderDetailsProperty(
    @ColumnInfo(name = "order_details_id")
    val orderDetailsId: Long,
    override val key: String,
    override val value: String
) : Property()
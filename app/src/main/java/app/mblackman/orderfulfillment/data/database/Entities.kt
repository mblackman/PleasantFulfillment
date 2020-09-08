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

    @ColumnInfo(name = "order_details_id")
    val orderDetailId: Long,

    @ColumnInfo(name = "product_id")
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

/**
 * A relational class between an [OrderDetails] an the containing [ProductSale]s.
 */
data class OrderDetailsWithProductSales(
    @Embedded
    val orderDetails: OrderDetails,

    @Relation(
        parentColumn = "order_details_id",
        entityColumn = "order_details_id",
        entity = ProductSale::class
    )
    val productSales: List<ProductSaleWithProduct>
)

/**
 * A relational class between a [ProductSale] and the containing [Product].
 */
data class ProductSaleWithProduct(
    @Embedded
    val productSale: ProductSale,

    @Relation(
        parentColumn = "product_id",
        entityColumn = "product_id",
        entity = Product::class
    )
    val product: Product
)
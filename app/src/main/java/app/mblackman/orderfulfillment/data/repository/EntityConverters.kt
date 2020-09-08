package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.Product
import app.mblackman.orderfulfillment.data.network.NetworkOrder
import app.mblackman.orderfulfillment.data.network.NetworkProduct

import app.mblackman.orderfulfillment.data.util.asDatabaseObject
import app.mblackman.orderfulfillment.data.util.toDate

/**
 * Converts and updates [NetworkOrder] to [OrderDetails].
 */
class OrderEntityConverter(private val adapterId: Int) :
    EntityConverter<OrderDetails, NetworkOrder> {
    /**
     * Gets how to handle conflicting entries.
     */
    override val conflictStrategy: ConflictStrategy = ConflictStrategy.UPDATE

    /**
     * Converts the network entity to adapter entity.
     *
     * @param item The item to convert.
     * @return The converted entity.
     */
    override fun toEntity(item: NetworkOrder): OrderDetails =
        item.asDatabaseObject(adapterId)

    /**
     * Updates data on the existing entity with the given new item's data.
     *
     * @param existingEntity The entity existing in the adapter.
     * @param item The new item to use for an update.
     *
     * @return A new entity with updated data.
     */
    override fun updateExisting(
        existingEntity: OrderDetails,
        item: NetworkOrder
    ): OrderDetails {
        return existingEntity.copy(
            orderDate = item.orderDate.toDate(),
            buyerName = item.buyerName,
            buyerEmail = item.buyerEmail,
            address = item.address,
        )
    }

}

/**
 * Converts and updates [NetworkProduct] to [Product].
 */
class ProductEntityConverter(private val adapterId: Int) :
    EntityConverter<Product, NetworkProduct> {
    /**
     * Gets how to handle conflicting entries.
     */
    override val conflictStrategy: ConflictStrategy = ConflictStrategy.UPDATE

    /**
     * Converts the network entity to adapter entity.
     *
     * @param item The item to convert.
     * @return The converted entity.
     */
    override fun toEntity(item: NetworkProduct): Product = item.asDatabaseObject(adapterId)

    /**
     * Updates data on the existing entity with the given new item's data.
     *
     * @param existingEntity The entity existing in the adapter.
     * @param item The new item to use for an update.
     *
     * @return A new entity with updated data.
     */
    override fun updateExisting(existingEntity: Product, item: NetworkProduct): Product =
        existingEntity.copy(
            name = item.name,
            description = item.description,
            imageUrl = item.imageUrl,
            price = item.price
        )

}
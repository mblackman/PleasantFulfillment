package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.database.AdapterEntity
import app.mblackman.orderfulfillment.data.network.NetworkItem

open class BaseRepository {
    protected suspend inline fun <E : AdapterEntity, N : NetworkItem> getAndUpdate(
        noinline getNewItems: suspend () -> Iterable<N>,
        noinline getExistingItems: (() -> Iterable<E>)?,
        noinline insertResults: (List<E>) -> Unit,
        entityConverter: EntityConverter<E, N>
    ) {
        val existingMappings =
            getExistingItems?.let { it() }?.associateBy(AdapterEntity::adapterEntityKey)

        insertResults(getNewItems().mapNotNull { item ->
            if (existingMappings?.containsKey(item.id) == true) {
                return@mapNotNull when (entityConverter.conflictStrategy) {
                    ConflictStrategy.SKIP -> null
                    ConflictStrategy.UPDATE -> entityConverter.updateExisting(
                        existingMappings.getValue(item.id),
                        item
                    )
                    ConflictStrategy.REPLACE -> entityConverter.toEntity(item)
                }
            }
            return@mapNotNull entityConverter.toEntity(item)
        })
    }
}

/**
 * How to handle a conflict when an existing entity is found in the database.
 */
enum class ConflictStrategy {
    SKIP, UPDATE, REPLACE
}

/**
 * Converts network entities to adapter entities.
 */
interface EntityConverter<E : AdapterEntity, N : NetworkItem> {
    /**
     * Gets how to handle conflicting entries.
     */
    val conflictStrategy: ConflictStrategy

    /**
     * Converts the network entity to adapter entity.
     *
     * @param item The item to convert.
     * @return The converted entity.
     */
    fun toEntity(item: N): E

    /**
     * Updates data on the existing entity with the given new item's data.
     *
     * @param existingEntity The entity existing in the adapter.
     * @param item The new item to use for an update.
     *
     * @return A new entity with updated data.
     */
    fun updateExisting(existingEntity: E, item: N): E
}
package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.database.AdapterEntity
import app.mblackman.orderfulfillment.data.network.NetworkItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseRepository {
    protected suspend inline fun <E : AdapterEntity, N : NetworkItem> getAndUpdate(
        noinline getNewItems: suspend () -> Iterable<N>,
        noinline getExistingItems: (() -> Iterable<E>)?,
        noinline insertResults: (List<E>) -> Unit,
        entityConverter: EntityConverter<E, N>
    ) {
        withContext(Dispatchers.IO) {
            val existingMappings =
                getExistingItems?.let { it() }?.associateBy(AdapterEntity::adapterEntityKey)

            insertResults(getNewItems().mapNotNull { item ->
                if (existingMappings?.containsKey(item.id) == true) {
                    if (!entityConverter.canUpdateExisting) {
                        return@mapNotNull null
                    }
                    return@mapNotNull entityConverter.updateExisting(
                        existingMappings.getValue(item.id),
                        item
                    )
                }
                return@mapNotNull entityConverter.toEntity(item)
            })
        }
    }
}

interface EntityConverter<E : AdapterEntity, N : NetworkItem> {
    val canUpdateExisting: Boolean
    fun toEntity(item: N): E
    fun updateExisting(existingEntity: E, item: N): E
}
package app.mblackman.orderfulfillment.data.common

import kotlin.reflect.KClass

/**
 * Stores data in persistent storage that's safe from outside processes from accessing the data.
 */
interface SafeItemStore {
    /**
     * Gets a item for the given key.
     *
     * @param key The key to look up the item by.
     * @param itemClass The class type of the item to get.
     * @return The item if found, else null.
     */
    fun <T : Any> getItem(key: String, itemClass: KClass<out T>): T?

    /**
     * Stores the given item with for the given key
     *
     * @param key The key to store the item under.
     * @param item The item to store.
     */
    fun storeItem(key: String, item: Any)

    /**
     * Clears any item for the given key.
     *
     * @param key The key for the item to clear.
     */
    fun clearItem(key: String)
}
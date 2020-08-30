package app.mblackman.orderfulfillment.data.database

/**
 * Represents a key-value pair property.
 */
open class Property(
    open val key: String? = null,
    open val value: String? = null
)

/**
 * Converts a list of properties into a map.
 */
fun List<Property>.asMap(): Map<String?, String?> = this.associateBy(Property::key, Property::value)

/**
 * Converts a mapping of key-value pairs into a property.
 */
fun Map<String, String>.asProperties(): List<Property> = this.map { Property(it.key, it.value) }
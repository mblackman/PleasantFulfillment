package app.mblackman.orderfulfillment.utils

import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

/**
 * A [BaseMatcher] that uses a custom equal check for all the items in a [Iterable]. Expects the items
 * in the [Iterable] to be in the same order.
 *
 * @param expected The expected collection to compare with.
 * @param matchCompare The compare function to compare items in the [Iterable].
 */
class CollectionMatcher<T>(
    private val expected: Iterable<T>,
    private val matchCompare: (T, Any?) -> Boolean
) : BaseMatcher<Iterable<T>>() {

    override fun describeTo(description: Description?) {
        description?.appendValue(expected)
    }

    override fun matches(item: Any?): Boolean {
        if (item == null || item !is Iterable<*>) {
            return false
        }

        val expectedIterator = expected.iterator()
        val resultIterator = item.iterator()

        while (expectedIterator.hasNext() || resultIterator.hasNext()) {
            if (expectedIterator.hasNext() != resultIterator.hasNext()) {
                // The size of collections is different.
                return false
            }

            val expectedItem = expectedIterator.next()
            val resultItem = resultIterator.next()

            if (!matchCompare(expectedItem, resultItem)) {
                return false
            }
        }

        return true
    }

}

/**
 * Creates an anonymous function that compares the properties of an expected value and the
 * actual result from a test.
 *
 * @param ignoreProperties A collection of properties to ignore in the comparison.
 */
inline fun <reified T : Any> propertyCompare(ignoreProperties: Collection<KProperty<*>>): (T, Any?) -> Boolean {
    return { expected: T, actual: Any? ->
        actual is T && T::class.memberProperties
            .filter { it !in ignoreProperties }
            .all { it.get(expected) == it.get(actual) }
    }
}

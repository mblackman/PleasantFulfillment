package app.mblackman.orderfulfillment.utils

import org.hamcrest.BaseMatcher
import org.hamcrest.Description

/**
 * A [BaseMatcher] that uses a custom equal check for all the items in a list. Expects the items
 * in the list to be in the same order.
 *
 * @param expected The expected collection to compare with.
 * @param matchCompare The compare function to compare items in the list.
 */
class ListMatcher<T>(
    private val expected: List<T>,
    private val matchCompare: (T, Any?) -> Boolean
) : BaseMatcher<List<T>>() {

    override fun describeTo(description: Description?) {
        description?.appendValue(expected)
    }

    override fun matches(item: Any?): Boolean {
        if (item == null || item !is List<*> || item.size != expected.size) {
            return false
        }

        for (i in (expected.indices)) {
            if (!this.matchCompare(expected[i], item[i])) {
                return false
            }
        }

        return true
    }

}

/**
 * Used to create a [ListMatcher] with a given compare function.
 *
 * @param matchCompare The function to compare items.
 */
class ListMatcherFactory<T>(private val matchCompare: (T, Any?) -> Boolean) {
    fun create(expected: List<T>): ListMatcher<T> =
        ListMatcher(expected, matchCompare)
}
package app.mblackman.orderfulfillment.data.common

/**
 * Represents the result of an operation.
 *
 * @param T The type of value returned by the result.
 */
sealed class Result<out T>

/**
 * A successful result with the result value.
 *
 * @param result The result of the operation.
 */
class Success<T>(val result: T) : Result<T>()

/**
 * A failed result from an operation.
 *
 * @param throwable The [Throwable] resulting from the failure.
 */
class Failure(val throwable: Throwable) : Result<Nothing>()
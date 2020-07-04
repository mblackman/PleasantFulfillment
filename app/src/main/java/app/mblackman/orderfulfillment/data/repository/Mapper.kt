package app.mblackman.orderfulfillment.data.repository

/**
 * Maps an input type to output type.
 */
interface Mapper<I, O> {
    /**
     * Maps the input object to the expected output type.
     */
    fun map(input: I): O
}
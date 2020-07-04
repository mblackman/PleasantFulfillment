package app.mblackman.orderfulfillment.data.repository

/**
 * Maps an input type to output type.
 */
interface Mapper<I, O> {

    /**
     * Maps the input object to the expected output type.
     *
     * @param input The input to map.
     * @return The mapped object based on data from the input.
     */
    fun map(input: I): O
}
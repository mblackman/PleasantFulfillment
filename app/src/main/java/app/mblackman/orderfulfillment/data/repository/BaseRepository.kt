package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.network.json.EtsyResponseWrapper
import retrofit2.Response
import timber.log.Timber

/**
 * Provides basic functionality for all repositories to use.
 */
open class BaseRepository {


    /**
     * Safely make Retrofit API calls, and wrap any errors with a provided message.
     *
     * @param call The Retrofit API call to make.
     * @param error The error string to provide on error.
     * @param pageSize The number of items to get per API response page.
     *
     * @return The value retrieved from the API.
     */
    suspend fun <T : Any> safeApiCall(
        call: suspend (limit: Int, offset: Int) -> Response<EtsyResponseWrapper<T>>,
        error: String,
        pageSize: Int = 50
    ): List<T>? {
        return try {
            var currentOffset = 0
            var isValid = true
            val returnValues = mutableListOf<T>()

            while (isValid) {
                val response = call(pageSize, currentOffset)
                Timber.i(response.toString())

                if (response.isSuccessful) {

                    returnValues.addAll(response.body()!!.results)

                    if (response.body()!!.count < pageSize) {
                        // The result contained fewer results than expected, which means no
                        // more items remain.
                        isValid = false
                    } else {
                        // Move to the next page.
                        currentOffset += pageSize
                    }
                } else {
                    Timber.e("Oops .. Something went wrong due to  $error")
                    return null
                }
            }

            return returnValues

        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }
}
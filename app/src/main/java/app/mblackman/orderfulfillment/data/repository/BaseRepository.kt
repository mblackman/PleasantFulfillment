package app.mblackman.orderfulfillment.data.repository

import retrofit2.Response
import timber.log.Timber
import java.io.IOException

open class BaseRepository {
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, error: String): T? {
        return try {
            val result = apiOutput(call, error)
            var output: T? = null
            when (result) {
                is Output.Success ->
                    output = result.output
                is Output.Error -> Timber.e("The $error and the ${result.exception}")
            }
            output
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    private suspend fun <T : Any> apiOutput(
        call: suspend () -> Response<T>,
        error: String
    ): Output<T> {
        val response = call.invoke()
        Timber.i(response.toString())
        return if (response.isSuccessful)
            Output.Success(response.body()!!)
        else
            Output.Error(IOException("Oops .. Something went wrong due to  $error"))
    }
}

sealed class Output<out T : Any> {
    data class Success<out T : Any>(val output: T) : Output<T>()
    data class Error(val exception: Exception) : Output<Nothing>()
}
package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.network.json.EtsyResponseWrapper
import io.mockk.every
import io.mockk.mockk
import retrofit2.Response

/**
 * Converts the list of an object into a wrapped etsy response.
 */
fun <T> List<T>.toEtsyResponse(): Response<EtsyResponseWrapper<T>> {
    val etsyResponse = EtsyResponseWrapper(this.size, this, "Test")
    val response = mockk<Response<EtsyResponseWrapper<T>>>(relaxed = true)
    every { response.isSuccessful } returns true
    every { response.body() } returns etsyResponse
    return response
}
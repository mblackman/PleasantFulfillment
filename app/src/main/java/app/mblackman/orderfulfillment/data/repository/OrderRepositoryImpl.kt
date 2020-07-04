package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.domain.OrderDetails
import app.mblackman.orderfulfillment.data.network.EtsyApiService
import app.mblackman.orderfulfillment.data.network.Receipt
import timber.log.Timber

class OrderRepositoryImpl(
    private val etsyApiService: EtsyApiService,
    private val receiptToOrderMapper: Mapper<Receipt, OrderDetails>
) : OrderRepository() {

    override suspend fun getOrderDetails(): List<OrderDetails>? {
        try {
            val userResponse = etsyApiService.getUserSelfAsync()

            if (userResponse.isSuccessful && userResponse.body()?.results?.size == 1) {
                val user = userResponse.body()!!.results.first()
                val receiptResponse = etsyApiService.getReceiptsAsync(
                    user.id,
                    EtsyApiService.ShipmentStatus.UNSHIPPED
                )

                if (receiptResponse.isSuccessful) {
                    return receiptResponse.body()!!.results.map { receiptToOrderMapper.map(it) }
                }
            }

        } catch (e: Exception) {
            Timber.log(1, e)
        }

        return null
    }
}
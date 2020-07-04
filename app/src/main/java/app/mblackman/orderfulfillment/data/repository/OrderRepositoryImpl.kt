package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.network.EtsyApiService
import app.mblackman.orderfulfillment.data.network.Receipt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Implementation of OrderRepository that fetches order details from the web
 * and persists them in a database.
 */
class OrderRepositoryImpl(
    private val etsyApiService: EtsyApiService,
    private val storeDatabase: StoreDatabase,
    private val receiptToOrderMapper: Mapper<Receipt, OrderDetails>,
    private val scope: CoroutineScope
) : OrderRepository() {

    /**
     * Gets the live data collection of the order details.
     */
    override fun getOrderDetails(): LiveData<List<OrderDetails>> {
        scope.launch {
            try {
                val userResponse = etsyApiService.getUserSelfAsync()

                if (userResponse.isSuccessful && userResponse.body()?.results?.size == 1) {
                    val user = userResponse.body()!!.results.first()
                    val receiptResponse = etsyApiService.getReceiptsAsync(
                        user.id,
                        EtsyApiService.ShipmentStatus.UNSHIPPED
                    )

                    if (receiptResponse.isSuccessful) {
                        val mappedResults =
                            receiptResponse.body()!!.results.map { receiptToOrderMapper.map(it) }
                        storeDatabase.storeDao.insertAll(mappedResults)
                    }
                }

            } catch (e: Exception) {
                Timber.log(1, e)
            }
        }

        return storeDatabase.storeDao.getOrderDetails()
    }
}
package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.network.EtsyApiService
import app.mblackman.orderfulfillment.data.network.Receipt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of OrderRepository that fetches order details from the web
 * and persists them in a database.
 */
class OrderRepositoryImpl(
    private val etsyApiService: EtsyApiService,
    private val storeDatabase: StoreDatabase,
    private val receiptToOrderMapper: Mapper<Receipt, OrderDetails>
) : OrderRepository() {

    /**
     * Gets the live data collection of the order details.
     */
    override suspend fun getOrderDetails(): LiveData<List<OrderDetails>> {

        val selfShop = safeApiCall(
            call = { etsyApiService.getShopSelfAsync().await() },
            error = "Failed to get self user."
        )

        if (selfShop?.count == 1) {
            val receipts = safeApiCall(
                call = { etsyApiService.getReceiptsAsync(selfShop.results.first().id).await() },
                error = "Failed to get receipts."
            )

            receipts?.results?.let {
                withContext(Dispatchers.IO) {
                    storeDatabase.storeDao.insertAll(it.map { receipt ->
                        receiptToOrderMapper.map(
                            receipt
                        )
                    })
                }
            }
        }

        return storeDatabase.storeDao.getOrderDetails()
    }
}
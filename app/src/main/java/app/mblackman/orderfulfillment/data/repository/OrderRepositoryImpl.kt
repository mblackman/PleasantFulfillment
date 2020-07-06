package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.network.EtsyApiService
import app.mblackman.orderfulfillment.data.network.json.Receipt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of OrderRepository that fetches order details from the web
 * and persists them in a database.
 */
class OrderRepositoryImpl(
    private val etsyApiService: EtsyApiService,
    private val storeDatabase: StoreDatabase,
    private val receiptToOrderDetailsMapper: Mapper<Receipt, OrderDetails>,
    private val orderDetailToOrderMapper: Mapper<OrderDetails, Order>
) : OrderRepository() {

    /**
     * Gets the live data collection of the order details.
     */
    override suspend fun getOrderDetails(): LiveData<List<Order>> {

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
                        receiptToOrderDetailsMapper.map(
                            receipt
                        )
                    })
                }
            }
        }

        return Transformations.map(storeDatabase.storeDao.getOrderDetails()) {
            it.map { orderDetails -> orderDetailToOrderMapper.map(orderDetails) }
        }
    }
}
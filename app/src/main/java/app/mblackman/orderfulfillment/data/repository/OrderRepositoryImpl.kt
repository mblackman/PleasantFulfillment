package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.domain.Shop
import app.mblackman.orderfulfillment.data.network.etsy.EtsyApiService
import app.mblackman.orderfulfillment.data.network.etsy.json.Receipt
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
     *
     * @param shop The shop to get order details from.
     * @return A live data with the collection of order details.
     */
    override suspend fun getOrderDetails(shop: Shop): LiveData<List<Order>> {

        val receipts = safeApiCall(
            call = { limit, offset ->
                etsyApiService.findAllReceiptsAsync(
                    shop.id,
                    limit,
                    offset
                ).await()
            },
            error = "Failed to get receipts."
        )

        receipts?.let {
            withContext(Dispatchers.IO) {
                storeDatabase.storeDao.insertAll(it.map { receipt ->
                    receiptToOrderDetailsMapper.map(
                        receipt
                    )
                })
            }
        }

        return Transformations.map(storeDatabase.storeDao.getOrderDetails()) {
            it.map { orderDetails -> orderDetailToOrderMapper.map(orderDetails) }
        }
    }
}
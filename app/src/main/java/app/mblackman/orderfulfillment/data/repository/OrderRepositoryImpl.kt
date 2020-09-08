package app.mblackman.orderfulfillment.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import app.mblackman.orderfulfillment.data.database.ProductSale
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.database.getDatabase
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.network.StoreAdapter
import app.mblackman.orderfulfillment.data.network.etsy.EtsyStoreAdapter
import app.mblackman.orderfulfillment.data.util.DefaultPrimaryKey
import app.mblackman.orderfulfillment.data.util.asDomainObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Implementation of OrderRepository that fetches order details from the web
 * and persists them in a database.
 */
class OrderRepositoryImpl(
    private val storeAdapter: StoreAdapter,
    private val storeDatabase: StoreDatabase
) : OrderRepository() {
    private val orderDataSourceFactory = storeDatabase.orderDetailsDao.getOrderDetailsWithProducts()

    companion object {
        /**
         * Creates a [OrderRepository] from the given [context].
         *
         * @param  context The context to create the [OrderRepository] from.
         * @return The created [OrderRepository].
         */
        fun create(context: Context): OrderRepository {
            return OrderRepositoryImpl(
                EtsyStoreAdapter.create(context),
                getDatabase(context)
            )
        }
    }

    /**
     * Gets the list of orders.
     */
    override val orderDetails: LiveData<PagedList<Order>> =
        orderDataSourceFactory.mapByPage {
            it.map { orderDetails ->
                orderDetails.asDomainObject()
            }
        }.toLiveData(pageSize = 50)

    /**
     * Gets the latest order detail data and stores it.
     */
    override suspend fun updateOrderDetails() {
        withContext(Dispatchers.IO) {
            getAndUpdate(
                storeAdapter::getOrders,
                { storeDatabase.orderDetailsDao.getOrderDetailsByAdapter(storeAdapter.adapterId) },
                { results -> storeDatabase.orderDetailsDao.insertAll(results) },
                OrderEntityConverter(storeAdapter.adapterId)
            )

            getAndUpdate(
                storeAdapter::getProducts,
                { storeDatabase.productDao.getProductByAdapter(storeAdapter.adapterId) },
                { results -> storeDatabase.productDao.insertAll(results) },
                ProductEntityConverter(storeAdapter.adapterId)
            )

            storeDatabase.productSaleDao.insertAll(storeAdapter.getProductSales().mapNotNull {
                val orderDetailsId = storeDatabase.orderDetailsDao.getOrderDetailsId(
                    storeAdapter.adapterId,
                    it.orderId
                )
                val productId =
                    storeDatabase.productDao.getProductId(storeAdapter.adapterId, it.productId)

                if (orderDetailsId == null || productId == null) {
                    if (orderDetailsId == null) {
                        Timber.e("Could not find order details with adapter: ${storeAdapter.adapterId} and adapter order id: ${it.orderId}")
                    }
                    if (productId == null) {
                        Timber.e("Could not find product with adapter: ${storeAdapter.adapterId} and adapter order id: ${it.productId}")
                    }
                    return@mapNotNull null
                } else {
                    return@mapNotNull ProductSale(
                        DefaultPrimaryKey,
                        storeAdapter.adapterId,
                        it.id,
                        orderDetailsId,
                        productId,
                        it.quantity
                    )
                }
            })


        }
    }

}
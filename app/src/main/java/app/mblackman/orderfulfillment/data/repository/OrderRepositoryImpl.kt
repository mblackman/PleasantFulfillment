package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import app.mblackman.orderfulfillment.data.database.ProductSale
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.network.StoreAdapter
import app.mblackman.orderfulfillment.data.util.DefaultPrimaryKey
import app.mblackman.orderfulfillment.data.util.asDatabaseObject
import app.mblackman.orderfulfillment.data.util.asDomainObject
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of OrderRepository that fetches order details from the web
 * and persists them in a database.
 */
class OrderRepositoryImpl @Inject constructor(
    private val storeAdapter: StoreAdapter,
    private val storeDatabase: StoreDatabase
) : OrderRepository {

    private val _hasValidSession = MutableLiveData<Boolean>()

    override val hasValidSession: LiveData<Boolean>
        get() = _hasValidSession

    /**
     * Gets the list of orders.
     */
    override val orderDetails: LiveData<PagedList<Order>> =
        storeDatabase.orderDetailsDao.getOrderDetailsWithProducts()
            .mapByPage {
                it.map { orderDetails ->
                    orderDetails.asDomainObject()
                }
            }.toLiveData(pageSize = 50)

    /**
     * Gets the latest order detail data and stores it.
     */
    override suspend fun updateOrderDetails() {
        if (!validateStoreAdapter()) return

        storeAdapter.getOrders()?.let {
            storeDatabase.orderDetailsDao.insertAll(it.map { order ->
                order.asDatabaseObject(
                    storeAdapter.adapterId
                )
            })
        }

        if (!validateStoreAdapter()) return

        storeAdapter.getProducts()?.let {
            storeDatabase.productDao.insertAll(it.map { product ->
                product.asDatabaseObject(
                    storeAdapter.adapterId
                )
            })
        }

        if (!validateStoreAdapter()) return

        storeAdapter.getProductSales()?.let { sales ->
            storeDatabase.productSaleDao.insertAll(sales.mapNotNull {
                val orderDetailsId = storeDatabase.orderDetailsDao.getOrderDetailsId(
                    storeAdapter.adapterId,
                    it.orderId
                )
                val productId =
                    storeDatabase.productDao.getProductId(
                        storeAdapter.adapterId,
                        it.productId
                    )

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

    private suspend fun validateStoreAdapter(): Boolean {
        if (storeAdapter.hasValidSession) {
            if (_hasValidSession.value != true) {
                _hasValidSession.postValue(true)
            }
            return true
        }

        storeAdapter.initialize()

        if (storeAdapter.hasValidSession) {
            _hasValidSession.postValue(true)
            return true
        }

        _hasValidSession.postValue(false)
        return false
    }
}
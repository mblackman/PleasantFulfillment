package app.mblackman.orderfulfillment.sharedTest

import app.mblackman.orderfulfillment.data.common.Failure
import app.mblackman.orderfulfillment.data.common.Result
import app.mblackman.orderfulfillment.data.common.Success
import app.mblackman.orderfulfillment.data.network.NetworkOrder
import app.mblackman.orderfulfillment.data.network.NetworkProduct
import app.mblackman.orderfulfillment.data.network.NetworkProductSale
import app.mblackman.orderfulfillment.data.network.StoreAdapter

class TestStoreAdapter(
    var orders: List<NetworkOrder>? = null,
    var products: List<NetworkProduct>? = null,
    var productSales: List<NetworkProductSale>? = null
) : StoreAdapter {

    override val adapterId: Int = -1

    companion object {
        fun empty() = TestStoreAdapter(
            orders = emptyList(),
            products = emptyList(),
            productSales = emptyList()
        )

        fun singleNetworkOrder(): TestStoreAdapter =
            TestStoreAdapter(
                orders = listOf(NetworkObjectUtils.createOrder(id = 1)),
                products = listOf(NetworkObjectUtils.createProduct(id = 1)),
                productSales = listOf(
                    NetworkObjectUtils.createProductSale(
                        productId = 1,
                        orderId = 1
                    )
                )
            )
    }

    override suspend fun getOrders(): Result<List<NetworkOrder>> = getResult(orders)

    override suspend fun getProducts(): Result<List<NetworkProduct>> = getResult(products)

    override suspend fun getProductSales(): Result<List<NetworkProductSale>> =
        getResult(productSales)

    private fun <T> getResult(value: T?): Result<T> =
        if (value == null)
            Failure(Exception("Mock failure"))
        else
            Success(value)
}
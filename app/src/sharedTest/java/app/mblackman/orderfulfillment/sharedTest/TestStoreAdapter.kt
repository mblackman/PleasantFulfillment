package app.mblackman.orderfulfillment.sharedTest

import app.mblackman.orderfulfillment.data.network.NetworkOrder
import app.mblackman.orderfulfillment.data.network.NetworkProduct
import app.mblackman.orderfulfillment.data.network.NetworkProductSale
import app.mblackman.orderfulfillment.data.network.StoreAdapter

class TestStoreAdapter(
    var orders: List<NetworkOrder>? = null,
    var products: List<NetworkProduct>? = null,
    var productSales: List<NetworkProductSale>? = null,
    override val hasValidSession: Boolean = true
) : StoreAdapter {

    override val adapterId: Int = -1

    /**
     * Initializes the [StoreAdapter] to start any services and get important information.
     */
    override suspend fun initialize() {

    }

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

    override suspend fun getOrders(): List<NetworkOrder> = orders ?: emptyList()

    override suspend fun getProducts(): List<NetworkProduct> = products ?: emptyList()

    override suspend fun getProductSales(): List<NetworkProductSale> = productSales ?: emptyList()
}
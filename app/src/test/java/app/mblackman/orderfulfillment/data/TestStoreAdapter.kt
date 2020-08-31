package app.mblackman.orderfulfillment.data

import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import app.mblackman.orderfulfillment.data.network.NetworkOrder
import app.mblackman.orderfulfillment.data.network.NetworkProduct
import app.mblackman.orderfulfillment.data.network.NetworkProductSale
import app.mblackman.orderfulfillment.data.network.StoreAdapter
import java.time.LocalDateTime

class TestStoreAdapter(private val orders: List<NetworkOrder>) : StoreAdapter {

    override val adapterId: Int = -1

    companion object {
        fun emptyNetworkOrders(): TestStoreAdapter = TestStoreAdapter(orders = emptyList())
        fun singleNetworkOrder(): TestStoreAdapter =
            TestStoreAdapter(
                orders = listOf(
                    NetworkOrder(
                        1,
                        OrderStatus.Purchased,
                        LocalDateTime.now(),
                        "Test",
                        "Test",
                        Address(
                            "Test Name",
                            "First Line",
                            "Second Line",
                            "Test City",
                            "Test State",
                            "Test Zip"
                        ),
                        null
                    )
                )
            )
    }

    override suspend fun getOrders(): List<NetworkOrder> = orders

    override suspend fun getProducts(): List<NetworkProduct> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductSales(): List<NetworkProductSale> {
        TODO("Not yet implemented")
    }
}
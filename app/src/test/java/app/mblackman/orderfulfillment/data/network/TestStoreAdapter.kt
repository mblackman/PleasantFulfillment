package app.mblackman.orderfulfillment.data.network

import app.mblackman.orderfulfillment.sharedTest.NetworkObjectUtils

class TestStoreAdapter(var orders: List<NetworkOrder>) : StoreAdapter {

    override val adapterId: Int = -1

    companion object {
        fun emptyNetworkOrders(): TestStoreAdapter = TestStoreAdapter(orders = emptyList())
        fun singleNetworkOrder(): TestStoreAdapter =
            TestStoreAdapter(orders = listOf(NetworkObjectUtils.createNetworkOrder(id = 1)))
    }

    override suspend fun getOrders(): List<NetworkOrder> = orders

    override suspend fun getProducts(): List<NetworkProduct> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductSales(): List<NetworkProductSale> {
        TODO("Not yet implemented")
    }
}
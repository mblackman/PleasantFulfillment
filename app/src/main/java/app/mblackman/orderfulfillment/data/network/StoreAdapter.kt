package app.mblackman.orderfulfillment.data.network

import app.mblackman.orderfulfillment.data.common.Result

/**
 * Represents an adapter for a storefront data source.
 */
interface StoreAdapter {
    val adapterId: Int

    /**
     * Gets the list of orders.
     */
    suspend fun getOrders(): Result<List<NetworkOrder>>

    /**
     * Gets the list of products.
     */
    suspend fun getProducts(): Result<List<NetworkProduct>>

    /**
     * Gets the list of product sales.
     */
    suspend fun getProductSales(): Result<List<NetworkProductSale>>
}
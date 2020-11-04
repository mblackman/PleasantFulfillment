package app.mblackman.orderfulfillment.data.network

/**
 * Represents an adapter for a storefront data source.
 */
interface StoreAdapter {
    val adapterId: Int

    val hasValidSession: Boolean

    /**
     * Initializes the [StoreAdapter] to start any services and get important information.
     */
    suspend fun initialize()

    /**
     * Gets the list of orders.
     */
    suspend fun getOrders(): List<NetworkOrder>?

    /**
     * Gets the list of products.
     */
    suspend fun getProducts(): List<NetworkProduct>?

    /**
     * Gets the list of product sales.
     */
    suspend fun getProductSales(): List<NetworkProductSale>?
}
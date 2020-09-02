package app.mblackman.orderfulfillment.data.repository

/**
 * Represents a repository to interact with Product and ProductSale
 */
abstract class ProductRepository : BaseRepository() {

    /**
     * Gets the latest ProductSales and inserts them to the database.
     */
    abstract suspend fun updateProductSales()
}
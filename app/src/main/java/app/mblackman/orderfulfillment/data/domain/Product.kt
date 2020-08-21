package app.mblackman.orderfulfillment.data.domain

/**
 * Represents a product for sale.
 */
data class Product(
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Double?
)

/**
 * Represents a sale of a product.
 */
data class ProductSale(
    val id: Int,
    val product: Product,
    val quantity: Int
)
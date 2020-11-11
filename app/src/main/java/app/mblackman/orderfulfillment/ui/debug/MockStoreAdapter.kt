package app.mblackman.orderfulfillment.ui.debug

import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import app.mblackman.orderfulfillment.data.network.NetworkOrder
import app.mblackman.orderfulfillment.data.network.NetworkProduct
import app.mblackman.orderfulfillment.data.network.NetworkProductSale
import app.mblackman.orderfulfillment.data.network.StoreAdapter
import java.time.LocalDateTime

class MockStoreAdapter(
    numOrders: Int = 10,
    numSalesPerOrder: Int = 3,
    override val hasValidSession: Boolean = true
) : StoreAdapter {

    override val adapterId: Int = MockAdapterId

    /**
     * Initializes the [StoreAdapter] to start any services and get important information.
     */
    override suspend fun initialize() {

    }

    companion object {
        const val MockAdapterId = 0
    }

    private val fellowship = listOf(
        MockPerson("Samwise Gamgee", "2breakfast@gmail.com"),
        MockPerson("Gandalf the Grey", "shallnotpass@gmail.com"),
        MockPerson("Legolas", "betterThanU@gmail.com"),
        MockPerson("Gimli", "stillCountsAs1@gmail.com"),
        MockPerson("Boromir", "arrowMagnet@gmail.com"),
        MockPerson("Peregrin", "luvs2smoke@gmail.com"),
        MockPerson("Frodo Baggins", "itchyFeet@gmail.com"),
        MockPerson("Aragorn", "rangerDanger@gmail.com")
    )

    private val products = listOf(
        NetworkProduct(
            1,
            "Leaf Pipe",
            "A pipe to smoke that good leaf.",
            "https://www.gadgetsville.store/wp-content/uploads/2017/12/13247.jpg",
            15.95
        ),
        NetworkProduct(
            2,
            "Stinger",
            "A sword that glows blue when orcs are near.",
            "https://vignette.wikia.nocookie.net/lotr/images/5/5d/Sting_with_scabbard.jpg/revision/latest/top-crop/width/720/height/900?cb=20140527085852",
            5000.95
        ),
        NetworkProduct(
            3,
            "Potatoes",
            "Boil 'em, mash 'em, stick 'em in a stew.",
            "https://i.gifer.com/fetch/w300-preview/80/8065e35fe8d1c58fa7827650cbde3388.gif",
            3.95
        )
    )

    private val orders: List<NetworkOrder> by lazy {
        val testAddress = Address("Shire", "Shire Way", "MiddleEarth", "Shire", "ME", "1")

        (0..numOrders).map { i ->
            val person = fellowship[i % fellowship.size]
            NetworkOrder(
                i.toLong(),
                OrderStatus.Purchased,
                LocalDateTime.now(),
                person.name,
                person.email,
                testAddress,
                null
            )
        }
    }

    private val productSales: List<NetworkProductSale> by lazy {
        var index = 0L

        orders.flatMap { order ->
            (0..numSalesPerOrder).map { i ->
                NetworkProductSale(
                    index++,
                    products[i % products.size].id,
                    order.id,
                    1
                )
            }
        }
    }

    override suspend fun getOrders(): List<NetworkOrder> = orders

    override suspend fun getProducts(): List<NetworkProduct> = products

    override suspend fun getProductSales(): List<NetworkProductSale> = productSales

    data class MockPerson(val name: String, val email: String)
}
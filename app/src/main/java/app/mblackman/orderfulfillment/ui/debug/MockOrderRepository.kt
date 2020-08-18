package app.mblackman.orderfulfillment.ui.debug

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.mblackman.orderfulfillment.data.domain.*
import app.mblackman.orderfulfillment.data.repository.OrderRepository
import java.util.*

class MockOrderRepository : OrderRepository() {
    var liveData = MutableLiveData<List<Order>>()

    init {
        val testAddress = Address("Shire", "Shire Way", "MiddleEarth", "Shire", "ME", "1")
        val testSales = listOf(
            ProductSale(Product("Product 1", "Product 1 descriptor", "null", 15.95f), 1),
            ProductSale(Product("Product 2", "Product 2 descriptor", "null", 16.95f), 1),
            ProductSale(Product("Product 3", "Product 3 descriptor", "null", 17.95f), 1)
        )

        val orders = listOf(
            Order(
                1,
                "Test Order 1",
                Date(),
                "Samwise Gamgee",
                "2breakfast@gmail.com",
                testAddress,
                testSales
            ),
            Order(
                2,
                "Test Order 2",
                Date(),
                "Gandalf the Grey",
                "shallnotpass@gmail.com",
                testAddress,
                testSales
            ),
            Order(
                3,
                "Test Order 3",
                Date(),
                "Legolas",
                "betterThanU@gmail.com",
                testAddress,
                testSales
            ),
            Order(
                4,
                "Test Order 4",
                Date(),
                "Gimli",
                "stillCountsAs1@gmail.com",
                testAddress,
                testSales
            ),
            Order(
                5,
                "Test Order 5",
                Date(),
                "Boromir",
                "arrowMagnet@gmail.com",
                testAddress,
                testSales
            ),
            Order(
                6,
                "Test Order 6",
                Date(),
                "Peregrin",
                "luvs2smoke@gmail.com",
                testAddress,
                testSales
            ),
            Order(
                7,
                "Test Order 7",
                Date(),
                "Frodo Baggins",
                "itchyFeet@gmail.com",
                testAddress,
                testSales
            ),
            Order(
                8,
                "Test Order 8",
                Date(),
                "Aragorn",
                "rangerDanger@gmail.com",
                testAddress,
                testSales
            )
        )

        liveData.value = orders
    }

    override suspend fun getOrderDetails(shop: Shop): LiveData<List<Order>> = liveData
}
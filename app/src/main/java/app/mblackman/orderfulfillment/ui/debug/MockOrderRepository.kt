package app.mblackman.orderfulfillment.ui.debug

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.mblackman.orderfulfillment.data.domain.*
import app.mblackman.orderfulfillment.data.repository.OrderRepository
import java.time.LocalDate

class MockOrderRepository(
    numOrders: Int = 10,
    minProductSales: Int = 1,
    maxProductSales: Int = 10
) : OrderRepository() {

    private var liveData = MutableLiveData<List<Order>>()

    private val mockPeople = listOf(
        MockPerson("Samwise Gamgee", "2breakfast@gmail.com"),
        MockPerson("Gandalf the Grey", "shallnotpass@gmail.com"),
        MockPerson("Legolas", "betterThanU@gmail.com"),
        MockPerson("Gimli", "stillCountsAs1@gmail.com"),
        MockPerson("Boromir", "arrowMagnet@gmail.com"),
        MockPerson("Peregrin", "luvs2smoke@gmail.com"),
        MockPerson("Frodo Baggins", "itchyFeet@gmail.com"),
        MockPerson("Aragorn", "rangerDanger@gmail.com")
    )

    private val mockProducts = listOf(
        Product("Leaf Pipe", "A pipe to smoke that good leaf.", "null", 15.95f),
        Product("Stinger", "A sword that glows blue when orcs are near.", "null", 5000.95f),
        Product("Potatoes", "Boil 'em, mash 'em, stick 'em in a stew.", "null", 3.95f)
    )

    init {
        val testAddress = Address("Shire", "Shire Way", "MiddleEarth", "Shire", "ME", "1")

        val orders = (0..numOrders).map { i ->
            val numSales = (minProductSales..maxProductSales).random()
            val person = mockPeople[i % mockPeople.size]
            Order(
                i,
                "Test Order $i",
                LocalDate.now(),
                person.name,
                person.email,
                testAddress,
                (0..(i % mockProducts.size)).map { ProductSale(mockProducts[it], numSales) }
            )
        }

        liveData.value = orders
    }

    override suspend fun getOrderDetails(shop: Shop): LiveData<List<Order>> = liveData

    data class MockPerson(val name: String, val email: String)
}
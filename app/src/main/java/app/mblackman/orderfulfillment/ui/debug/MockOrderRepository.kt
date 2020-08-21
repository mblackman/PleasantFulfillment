package app.mblackman.orderfulfillment.ui.debug

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.mblackman.orderfulfillment.data.common.OrderStatus
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
        Product(
            "Leaf Pipe",
            "A pipe to smoke that good leaf.",
            "https://www.gadgetsville.store/wp-content/uploads/2017/12/13247.jpg",
            15.95
        ),
        Product(
            "Stinger",
            "A sword that glows blue when orcs are near.",
            "https://vignette.wikia.nocookie.net/lotr/images/5/5d/Sting_with_scabbard.jpg/revision/latest/top-crop/width/720/height/900?cb=20140527085852",
            5000.95
        ),
        Product(
            "Potatoes",
            "Boil 'em, mash 'em, stick 'em in a stew.",
            "https://i.gifer.com/fetch/w300-preview/80/8065e35fe8d1c58fa7827650cbde3388.gif",
            3.95
        )
    )

    init {
        val testAddress = Address("Shire", "Shire Way", "MiddleEarth", "Shire", "ME", "1")
        var currentProductId = 0

        val orders = (0..numOrders).map { i ->
            val numSales = (minProductSales..maxProductSales).random()
            val person = mockPeople[i % mockPeople.size]
            Order(
                i,
                "Test Order $i",
                OrderStatus.Purchased,
                LocalDate.now(),
                person.name,
                person.email,
                testAddress,
                (0..(i % mockProducts.size)).map {
                    ProductSale(
                        currentProductId++,
                        mockProducts[it],
                        numSales
                    )
                }
            )
        }

        liveData.value = orders
    }

    override suspend fun getOrderDetails(shop: Shop): LiveData<List<Order>> = liveData

    data class MockPerson(val name: String, val email: String)
}
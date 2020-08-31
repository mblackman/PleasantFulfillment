package app.mblackman.orderfulfillment.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.mblackman.orderfulfillment.data.common.OrderStatus
import app.mblackman.orderfulfillment.data.database.MockOrderDetailsDao
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.network.StoreAdapter
import app.mblackman.orderfulfillment.data.network.TestStoreAdapter
import app.mblackman.orderfulfillment.sharedTest.DatabaseObjectUtils
import app.mblackman.orderfulfillment.sharedTest.NetworkObjectUtils
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkObject
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class OrderRepositoryImplTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val database: StoreDatabase = mockk(relaxed = true)
    private lateinit var observer: Observer<List<Order>>

    @Before
    fun setUp() {
        observer = Observer<List<Order>> {}
    }

    @After
    fun after() {
        unmockkObject(database)
    }

    @Test
    fun getOrdersLoadFromWeb() {
        val repo = setupMocks(TestStoreAdapter.singleNetworkOrder())

        runBlocking { repo.updateOrderDetails() }
        val result = repo.orderDetails
        result.observeForever(observer)

        assertThat(
            "The user should have come from the database since it matches the config's user id.",
            result.value?.size, equalTo(1)
        )
    }

    @Test
    fun emptyWebRequest() {
        val repo = setupMocks(TestStoreAdapter.emptyNetworkOrders())

        runBlocking { repo.updateOrderDetails() }
        val result = repo.orderDetails
        result.observeForever(observer)

        assertThat(
            "No items should have been loaded into the repo.",
            result.value?.size, equalTo(0)
        )
    }

    @Test
    fun emptyWebRequest() {
        val repo = setupMocks(TestStoreAdapter.emptyNetworkOrders())

        runBlocking { repo.updateOrderDetails() }
        val result = repo.orderDetails
        result.observeForever(observer)

        Truth.assertWithMessage("No items should have been loaded from the adapter.")
            .that(result.value?.size)
            .isEqualTo(0)
    }

    /**
     * Verifies when an order is updated from an adapter, state is retained in the repository.
     */
    @Test
    fun getUpdatedOrderDetails() {
        val orderForeignId = 100L
        val updatedOrder = NetworkObjectUtils.createNetworkOrder(
            id = orderForeignId,
            orderStatus = OrderStatus.Purchased
        )
        val adapter = TestStoreAdapter(orders = listOf(updatedOrder))
        val existingOrderDetails = DatabaseObjectUtils.createOrderDetails(
            adapterId = adapter.adapterId,
            adapterEntityKey = orderForeignId,
            status = OrderStatus.Filled
        )
        val repo = setupMocks(adapter, listOf(existingOrderDetails))

        runBlocking { repo.updateOrderDetails() }
        val result = repo.orderDetails
        result.observeForever(observer)

        Truth.assertWithMessage("No items should have been loaded from the adapter.")
            .that(result.value)
            .isEqualTo(
                listOf(
                    existingOrderDetails.copy(status = OrderStatus.Filled).asDomainObject()
                )
            )
    }

    private fun setupMocks(
        storeAdapter: StoreAdapter,
        orderDetails: List<OrderDetails> = emptyList()
    ): OrderRepository {

        every { database.orderDetailsDao } returns MockOrderDetailsDao(orderDetails)

        return OrderRepositoryImpl(
            storeAdapter,
            database
        )
    }
}
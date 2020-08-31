package app.mblackman.orderfulfillment.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.mblackman.orderfulfillment.data.TestStoreAdapter
import app.mblackman.orderfulfillment.data.database.MockOrderDetailsDao
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.network.StoreAdapter
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkObject
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Assert.assertThat
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
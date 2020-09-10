package app.mblackman.orderfulfillment.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import app.mblackman.orderfulfillment.data.common.OrderStatus
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.sharedTest.DatabaseObjectUtils
import app.mblackman.orderfulfillment.sharedTest.NetworkObjectUtils
import app.mblackman.orderfulfillment.sharedTest.TestStoreAdapter
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class OrderRepositoryImplTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var observer: Observer<List<Order>>
    private lateinit var storeDatabase: StoreDatabase

    @Before
    fun setUp() {
        observer = Observer<List<Order>> {}
        val context = ApplicationProvider.getApplicationContext<Context>()
        storeDatabase = Room.inMemoryDatabaseBuilder(context, StoreDatabase::class.java).build()
    }

    @After
    @Throws(IOException::class)
    fun after() {
        storeDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun getOrdersLoadFromWeb() {
        val repo = OrderRepositoryImpl(TestStoreAdapter.singleNetworkOrder(), storeDatabase)

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
    @Throws(Exception::class)
    fun emptyWebRequest() {
        val repo = OrderRepositoryImpl(TestStoreAdapter.empty(), storeDatabase)

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
    @Throws(Exception::class)
    fun getUpdatedOrderDetails() {
        val orderForeignId = 100L
        val updatedOrder = NetworkObjectUtils.createOrder(
            id = orderForeignId,
            orderStatus = OrderStatus.Purchased
        )
        val adapter = TestStoreAdapter(orders = listOf(updatedOrder))
        val existingOrderDetails = DatabaseObjectUtils.createOrderDetails(
            adapterId = adapter.adapterId,
            adapterEntityKey = orderForeignId,
            status = OrderStatus.Filled,
        )
        val repo = OrderRepositoryImpl(adapter, storeDatabase)
        storeDatabase.orderDetailsDao.insertAll(listOf(existingOrderDetails))

        runBlocking { repo.updateOrderDetails() }
        val result = repo.orderDetails
        result.observeForever(observer)

        Truth.assertWithMessage("No new orders should have been created.")
            .that(result.value?.size)
            .isEqualTo(1)
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
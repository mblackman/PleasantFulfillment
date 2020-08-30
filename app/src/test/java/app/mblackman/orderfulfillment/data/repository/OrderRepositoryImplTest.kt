package app.mblackman.orderfulfillment.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.mblackman.orderfulfillment.data.TestStoreAdapter
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.OrderDetailsDao
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.network.StoreAdapter
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkObject
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Rule
import org.junit.Test


class OrderRepositoryImplTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val database: StoreDatabase = mockk(relaxed = true)

    @After
    fun after() {
        unmockkObject(database)
    }

    @Test
    fun getOrdersLoadFromWeb() {
        val observer = Observer<List<Order>> {}
        val repo = setupMocks(TestStoreAdapter.singleNetworkOrder())

        runBlocking { repo.updateOrderDetails() }
        val result = repo.orderDetails
        result.observeForever(observer)

        Truth.assertWithMessage("The user should have come from the database since it matches the config's user id.")
            .that(result.value?.size).isEqualTo(1)
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

    class MockOrderDetailsDao(private var orderDetails: List<OrderDetails>) : OrderDetailsDao {
        private val liveOrderDetails = MutableLiveData<List<OrderDetails>>()

        init {
            liveOrderDetails.value = orderDetails
        }

        override fun getOrderDetails(): LiveData<List<OrderDetails>> = liveOrderDetails

        override fun getOrderDetailsByAdapter(adapterId: Int): List<OrderDetails> =
            liveOrderDetails.value ?: emptyList()

        override fun insertAll(items: List<OrderDetails>) {
            this.orderDetails = this.orderDetails + items
            this.liveOrderDetails.value = this.orderDetails
        }

    }
}
package app.mblackman.orderfulfillment.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.StoreDao
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.data.domain.Shop
import app.mblackman.orderfulfillment.data.network.etsy.EtsyApiService
import app.mblackman.orderfulfillment.data.network.etsy.json.Receipt
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Rule
import org.junit.Test


class OrderRepositoryImplTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val etsyApiService: EtsyApiService = mockk()
    private val database: StoreDatabase = mockk(relaxed = true)

    @After
    fun after() {
        unmockkObject(etsyApiService)
        unmockkObject(database)
    }

    @Test
    fun getOrdersLoadFromWeb() {
        val observer = Observer<List<Order>> {}

        val shop = Shop(1)
        val receipts = listOf(Receipt(1))
        val repo = setupMocks(shop.id, receipts)

        val result = runBlocking { repo.getOrderDetails(shop) }
        result.observeForever(observer)

        Truth.assertWithMessage("The user should have come from the database since it matches the config's user id.")
            .that(result.value?.size).isEqualTo(1)
    }

    private fun setupMocks(
        shopId: Int,
        receipts: List<Receipt> = emptyList(),
        orderDetails: List<OrderDetails> = emptyList()
    ): OrderRepository {
        every {
            etsyApiService.findAllReceiptsAsync(
                shopId,
                any(),
                any()
            )
        } returns CompletableDeferred(receipts.toEtsyResponse())

        every { database.storeDao } returns MockStoreDao(orderDetails)

        return OrderRepositoryImpl(
            etsyApiService,
            database,
            ReceiptToOrderMapper(),
            OrderDetailsToOrderMapper()
        )
    }

    class MockStoreDao(private var orderDetails: List<OrderDetails>) : StoreDao {
        private val liveOrderDetails = MutableLiveData<List<OrderDetails>>()

        init {
            liveOrderDetails.value = orderDetails
        }

        override fun getOrderDetails(): LiveData<List<OrderDetails>> = liveOrderDetails

        override fun insertAll(orderDetails: List<OrderDetails>) {
            this.orderDetails = this.orderDetails + orderDetails
            this.liveOrderDetails.value = this.orderDetails
        }

    }
}
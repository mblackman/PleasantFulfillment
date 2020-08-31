package app.mblackman.oderfulfillment.data.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import app.mblackman.oderfulfillment.utils.ListMatcherFactory
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.OrderDetailsDao
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4ClassRunner::class)
class OrderDetailsDaoTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var observer: Observer<List<OrderDetails>>
    private lateinit var orderDetailsDao: OrderDetailsDao
    private lateinit var storeDatabase: StoreDatabase

    private val detailsListMatcherFactory = ListMatcherFactory<OrderDetails> { expected, result ->
        orderDetailsCompare(
            expected,
            result
        )
    }

    @Before
    fun setUp() {
        observer = Observer<List<OrderDetails>> {}
        val context = ApplicationProvider.getApplicationContext<Context>()
        storeDatabase = Room.inMemoryDatabaseBuilder(context, StoreDatabase::class.java).build()
        orderDetailsDao = storeDatabase.orderDetailsDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        storeDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeOrderDetailAndReadList() {
        val orderDetails = Utils.createOrderDetails()

        orderDetailsDao.insertAll(listOf(orderDetails))
        val storedOrderDetails = orderDetailsDao.getOrderDetails()
        storedOrderDetails.observeForever(observer)

        assertThat(storedOrderDetails.value, detailsListMatcherFactory.create(listOf(orderDetails)))
    }

    @Test
    @Throws(Exception::class)
    fun getByAdapterMatchesAdapterId() {
        val adapter1OrderDetails = createAdapterUsers(1, 3)
        val adapter2OrderDetails = createAdapterUsers(2, 3)
        val adapter3OrderDetails = createAdapterUsers(3, 3)
        val allItems = adapter1OrderDetails + adapter2OrderDetails + adapter3OrderDetails

        orderDetailsDao.insertAll(allItems)
        val storedOrderDetailsAdapter1 = orderDetailsDao.getOrderDetailsByAdapter(1)
        val allStoredOrderDetails = orderDetailsDao.getOrderDetails()
        allStoredOrderDetails.observeForever(observer)

        assertThat(allStoredOrderDetails.value, detailsListMatcherFactory.create(allItems))
        assertThat(
            storedOrderDetailsAdapter1,
            detailsListMatcherFactory.create(adapter1OrderDetails)
        )
    }

    /**
     * Validates the database maintains a single [OrderDetails] with a certain adapter id and
     * adapter entity id.
     */
    @Test()
    @Throws(Exception::class)
    fun singleRemoteEntityUpdate() {
        val adapterId = 1
        val adapterEntityId = 100L
        val originalOrderDetails = Utils.createOrderDetails(
            adapterId = adapterId,
            adapterEntityKey = adapterEntityId,
            buyerName = "original"
        )
        val replacedOrderDetails = Utils.createOrderDetails(
            adapterId = adapterId,
            adapterEntityKey = adapterEntityId,
            buyerName = "replaced"
        )

        orderDetailsDao.insertAll(listOf(originalOrderDetails))
        orderDetailsDao.insertAll(listOf(replacedOrderDetails))
        val storedOrderDetails = orderDetailsDao.getOrderDetails()
        storedOrderDetails.observeForever(observer)

        assertThat(
            storedOrderDetails.value,
            detailsListMatcherFactory.create(listOf(replacedOrderDetails))
        )
    }

    private fun createAdapterUsers(adapterId: Int, numUsers: Int = 3) =
        (1..numUsers).map {
            val id = it.toLong()
            Utils.createOrderDetails(
                adapterId = adapterId,
                adapterEntityKey = id,
                buyerName = "Adapter $adapterId User $it"
            )
        }
}
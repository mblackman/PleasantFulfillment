package app.mblackman.oderfulfillment.data.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import app.mblackman.orderfulfillment.data.database.OrderDetails
import app.mblackman.orderfulfillment.data.database.OrderDetailsDao
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import org.hamcrest.core.IsEqual.equalTo
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
        val orderDetails = Utils.createOrderDetails(orderDetailsId = 1)

        orderDetailsDao.insertAll(listOf(orderDetails))
        val storedOrderDetails = orderDetailsDao.getOrderDetails()
        storedOrderDetails.observeForever(observer)

        assertThat(
            "Only a single element was expected",
            storedOrderDetails.value?.size,
            equalTo(1)
        )

        assertThat(
            "Order details from database should match properties from inserted item.",
            storedOrderDetails.value?.get(0),
            equalTo(orderDetails)
        )
    }
}
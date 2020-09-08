package app.mblackman.orderfulfillment.data.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import app.mblackman.orderfulfillment.sharedTest.DatabaseObjectUtils
import app.mblackman.orderfulfillment.utils.CollectionMatcher
import app.mblackman.orderfulfillment.utils.propertyCompare
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

    val orderDetailsCompare =
        propertyCompare<OrderDetails>(ignoreProperties = listOf(OrderDetails::localId))
    val productSaleCompare =
        propertyCompare<ProductSale>(ignoreProperties = listOf(ProductSale::localId))
    val productCompare = propertyCompare<Product>(ignoreProperties = listOf(Product::localId))

    private fun orderDetailsMatcher(expected: Iterable<OrderDetails>) =
        CollectionMatcher(
            expected,
            orderDetailsCompare
        )

    private fun orderDetailsWithProductSalesMatcher(expected: Iterable<OrderDetailsWithProductSales>) =
        CollectionMatcher(expected) { expect, actual ->
            if (actual !is OrderDetailsWithProductSales) {
                return@CollectionMatcher false
            }
            return@CollectionMatcher orderDetailsCompare(expect.orderDetails, actual.orderDetails)
                    && expect.productSales.size == actual.productSales.size
                    && expect.productSales.zip(actual.productSales).all { (ps1, ps2) ->
                productSaleCompare(ps1.productSale, ps2.productSale) && productCompare(
                    ps1.product,
                    ps2.product
                )
            }
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
        val orderDetails = DatabaseObjectUtils.createOrderDetails()

        orderDetailsDao.insertAll(listOf(orderDetails))
        val storedOrderDetails = orderDetailsDao.getOrderDetails()
        storedOrderDetails.observeForever(observer)

        assertThat(storedOrderDetails.value, orderDetailsMatcher(listOf(orderDetails)))
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

        assertThat(allStoredOrderDetails.value, orderDetailsMatcher(allItems))
        assertThat(
            storedOrderDetailsAdapter1,
            orderDetailsMatcher(adapter1OrderDetails)
        )
    }

    @Test
    @Throws(Exception::class)
    fun getOrderDetailsAndProductSales() {
        val orderDetails = DatabaseObjectUtils.createOrderDetails()
        val productSales = listOf(
            DatabaseObjectUtils.createProductSale(
                orderDetailsId = 1,
                adapterEntityKey = 1,
                productId = 1
            ),
            DatabaseObjectUtils.createProductSale(
                orderDetailsId = 1,
                adapterEntityKey = 2,
                productId = 2
            )
        )
        val products = listOf(
            DatabaseObjectUtils.createProduct(adapterEntityKey = 1),
            DatabaseObjectUtils.createProduct(adapterEntityKey = 2)
        )
        val expected = listOf(
            OrderDetailsWithProductSales(
                orderDetails, listOf(
                    ProductSaleWithProduct(productSales[0], products[0]),
                    ProductSaleWithProduct(productSales[1], products[1])
                )
            )
        )

        orderDetailsDao.insertAll(listOf(orderDetails))
        storeDatabase.productSaleDao.insertAll(productSales)
        storeDatabase.productDao.insertAll(products)
        val storedOrderDetails =
            LivePagedListBuilder(orderDetailsDao.getOrderDetailsWithProducts(), 20).build()
        val withProductSalesObserver = Observer<List<OrderDetailsWithProductSales>> {}
        storedOrderDetails.observeForever(withProductSalesObserver)

        assertThat(storedOrderDetails.value, orderDetailsWithProductSalesMatcher(expected))
    }

    /**
     * Validates the database maintains a single [OrderDetails] with a certain adapter id and
     * adapter entity id.
     */
    @Test
    @Throws(Exception::class)
    fun singleRemoteEntityUpdate() {
        val adapterId = 1
        val adapterEntityId = 100L
        val originalOrderDetails = DatabaseObjectUtils.createOrderDetails(
            adapterId = adapterId,
            adapterEntityKey = adapterEntityId,
            buyerName = "original"
        )
        val replacedOrderDetails = DatabaseObjectUtils.createOrderDetails(
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
            orderDetailsMatcher(listOf(replacedOrderDetails))
        )
    }

    private fun createAdapterUsers(adapterId: Int, numUsers: Int = 3) =
        (1..numUsers).map {
            val id = it.toLong()
            DatabaseObjectUtils.createOrderDetails(
                adapterId = adapterId,
                adapterEntityKey = id,
                buyerName = "Adapter $adapterId User $it"
            )
        }
}

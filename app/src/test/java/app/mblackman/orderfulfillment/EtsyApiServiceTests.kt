package app.mblackman.orderfulfillment

import android.content.Context
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class EtsyApiServiceTests {

    lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        //instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun get_unshipped_receipts() = runBlocking {
//        val apiService = EtsyServiceGenerator(SessionManager(instrumentationContext)).createService(EtsyApiService::class.java, object : AuthenticationListener { })
//        val receipts = apiService.getUnshippedReceipts().await()
//        assertNotNull(receipts)
    }
}

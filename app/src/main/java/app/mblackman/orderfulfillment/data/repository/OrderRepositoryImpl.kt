package app.mblackman.orderfulfillment.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import app.mblackman.orderfulfillment.data.domain.OrderDetails
import app.mblackman.orderfulfillment.data.network.EtsyApiService
import app.mblackman.orderfulfillment.data.network.Receipt
import app.mblackman.orderfulfillment.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderRepositoryImpl(
    private val etsyApiService: EtsyApiService,
    private val receiptToOrderMapper: Mapper<Receipt, OrderDetails>
) : OrderRepository() {

    private var job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun getOrderDetails(): LiveData<List<OrderDetails>> {
        TODO("Not yet implemented")
    }

    private fun getUnshippedReceipts() {
        coroutineScope.launch {
            try {
                val userResponse = etsyApiService.getUserSelfAsync()

                if (userResponse.results.size == 1) {
                    val user = userResponse.results.first()
                    val receipts = etsyApiService.getReceiptsAsync(
                        user.id,
                        EtsyApiService.ShipmentStatus.UNSHIPPED
                    )

                    if (receipts != null) {

                    }
                }

            } catch (e: Exception) {
                Log.e(MainViewModel::class.java.name, e.toString())
            }
        }
    }
}
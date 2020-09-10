package app.mblackman.orderfulfillment.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.mblackman.orderfulfillment.data.network.NetworkException
import app.mblackman.orderfulfillment.data.repository.OrderRepository
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject

class OrderWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    @Inject
    lateinit var repository: OrderRepository

    companion object {
        const val WORK_NAME = "RefreshOrdersWorker"
    }

    override suspend fun doWork(): Result = coroutineScope {

        try {
            repository.updateOrderDetails()
        } catch (networkException: NetworkException) {
            Timber.e(networkException)
            Result.failure()
        }

        Result.success()
    }

}
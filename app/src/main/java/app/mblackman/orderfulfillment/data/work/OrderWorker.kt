package app.mblackman.orderfulfillment.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.mblackman.orderfulfillment.data.network.NetworkException
import app.mblackman.orderfulfillment.data.repository.OrderRepositoryImpl
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

class OrderWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshOrdersWorker"
    }

    override suspend fun doWork(): Result = coroutineScope {
        val repo = OrderRepositoryImpl.create(applicationContext)

        try {
            repo.updateOrderDetails()
        } catch (networkException: NetworkException) {
            Timber.e(networkException)
            Result.failure()
        }

        Result.success()
    }

}
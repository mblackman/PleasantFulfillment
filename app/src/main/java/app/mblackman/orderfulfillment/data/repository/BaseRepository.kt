package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData

interface BaseRepository {
    val hasValidLogin: LiveData<Boolean>
}

package app.mblackman.orderfulfillment.data.repository

import androidx.lifecycle.LiveData

/**
 * The base functionality for a repository.
 */
interface BaseRepository {
    /**
     * Gets whether the current session is valid for use.
     */
    val hasValidSession: LiveData<Boolean>
}

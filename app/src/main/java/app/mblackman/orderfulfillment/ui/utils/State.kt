package app.mblackman.orderfulfillment.ui.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Contains state information for an order view object.
 */
class ExpandState {
    private val _isExpanded = MutableLiveData<Boolean>(false)

    val isExpanded: LiveData<Boolean>
        get() = _isExpanded

    /**
     * Toggles the expanded state.
     */
    fun toggleExpand() {
        this._isExpanded.value = !this._isExpanded.value!!
    }
}

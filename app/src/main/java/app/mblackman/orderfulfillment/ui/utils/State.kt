package app.mblackman.orderfulfillment.ui.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Contains state information for an order view object.
 */
class ExpandState {
    private val _isExpanded = MutableLiveData<Boolean>()

    val isExpanded: LiveData<Boolean>
        get() = _isExpanded

    init {
        _isExpanded.value = false
    }

    /**
     * Toggles the expanded state.
     */
    fun toggleExpand() {
        this._isExpanded.value = !this._isExpanded.value!!
    }
}
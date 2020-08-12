package app.mblackman.orderfulfillment.ui.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import app.mblackman.orderfulfillment.data.domain.Address


/**
 * Binding adapter used to display images from URL using Glide
 */
@BindingAdapter("address")
fun setAddress(textView: TextView, address: Address) {
    textView.text = "${address.name}\n${address.firstLine}\n${address.secondLine}"
}

@BindingConversion
fun convertBooleanToVisibility(visible: Boolean): Int {
    return if (visible) View.VISIBLE else View.GONE
}
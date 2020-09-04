package app.mblackman.orderfulfillment.ui.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat
import java.text.NumberFormat


/**
 * Binding adapter used to format an address.
 */
@SuppressLint("SetTextI18n")
@BindingAdapter("address")
fun setAddress(textView: TextView, address: Address) {
    textView.text = "${address.name}\n${address.firstLine}\n${address.secondLine}"
}

/**
 * Binding adapter used to truncate an int.
 */
@BindingAdapter("truncatedInt")
fun setTruncatedInt(textView: TextView, value: Int) {
    textView.text = DecimalFormat("00").format(value)
}

@BindingAdapter("statusButtonText")
fun setStatusButtonText(textView: TextView, value: OrderStatus) {
    textView.text = when (value) {
        OrderStatus.Purchased -> "Mark Filled"
        OrderStatus.Filled -> "Mark Not Filled"
        else -> "Don't do it"
    }
}

@BindingAdapter("cost")
fun setCost(textView: TextView, value: Double?) {
    val format = NumberFormat.getCurrencyInstance()
    textView.text = format.format(value)
}

@BindingAdapter("orderStatus")
fun setOrderStatus(imageView: ImageView, value: OrderStatus?) {
    val context = imageView.context
    imageView.imageTintList = when (value) {
        OrderStatus.Purchased -> context.getColorStateList(R.color.order_status_purchased_tint)
        OrderStatus.Filled -> context.getColorStateList(R.color.order_status_filled_tint)
        OrderStatus.Shipped -> context.getColorStateList(R.color.order_status_shipped_tint)
        OrderStatus.Delivered -> context.getColorStateList(R.color.order_status_delivered_tint)
        else -> context.getColorStateList(R.color.order_status_purchased_tint)
    }
}

@BindingAdapter("orderStatus")
fun setOrderStatus(textView: TextView, value: OrderStatus?) {
    textView.text = when (value) {
        OrderStatus.Purchased -> "Purchased"
        OrderStatus.Filled -> "Filled"
        OrderStatus.Shipped -> "Shipped"
        OrderStatus.Delivered -> "Delivered"
        else -> "What"
    }
}

@BindingConversion
fun convertBooleanToVisibility(visible: Boolean): Int {
    return if (visible) View.VISIBLE else View.GONE
}

/**
 * Provides a binding to load an image url with Glide. This handles showing a loading icon
 * and error icon.
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.hourglass_top_24px)
                    .error(R.drawable.broken_image_24px)
            )
            .into(imgView)
    }
}

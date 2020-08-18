package app.mblackman.orderfulfillment.ui.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.data.domain.Address
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat


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

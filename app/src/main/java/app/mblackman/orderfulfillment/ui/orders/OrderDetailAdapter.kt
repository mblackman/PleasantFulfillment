package app.mblackman.orderfulfillment.ui.orders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.databinding.ListItemOrderDetailsBinding

private const val UNKNOWN_VIEW_TYPE_ITEM = -1
private const val ITEM_VIEW_TYPE_ITEM = 0

/**
 * An adapter for order details and related items. Helps bind the order properties with
 * view items in a collection.
 *
 * @param context The context of the owner of this adapter.
 * @param lifecycleOwner The lifecycle owner for the owner of this adapter.
 */
class OrderDetailAdapter(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) : PagedListAdapter<Order, RecyclerView.ViewHolder>(diffCallback) {
    private val _orderStatusRequestChanged = MutableLiveData<Order>()

    val orderStatusRequestChanged: LiveData<Order>
        get() = _orderStatusRequestChanged

    /**
     * Checks for differences between orders.
     */
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(
                oldItem: Order,
                newItem: Order
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Order,
                newItem: Order
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BindingViewHolder -> {
                val item = getItem(position) as Order
                holder.bind(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM_VIEW_TYPE_ITEM -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemOrderDetailsBinding.inflate(layoutInflater, parent, false)
                BindingViewHolder(binding)
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Order -> ITEM_VIEW_TYPE_ITEM
            else -> UNKNOWN_VIEW_TYPE_ITEM
        }
    }

    /**
     * View Holder for the order items being created. Handles binding the views to the orders.
     */
    inner class BindingViewHolder(private val binding: ListItemOrderDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the view to the given order.
         *
         * @param item The order to bind to.
         */
        fun bind(item: Order) {
            binding.order = item

            with(ProductSaleAdapter(lifecycleOwner, item.isExpanded)) {
                val decorator =
                    DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                        setDrawable(ContextCompat.getDrawable(context, R.drawable.divider)!!)
                    }
                binding.productSales.adapter = this
                binding.productSales.addItemDecoration(decorator, 0)

                binding.statusUpdateButton.setOnClickListener {
                    _orderStatusRequestChanged.postValue(item)
                }

                item.productSales?.let { this.setItems(it) }
            }

            binding.lifecycleOwner = lifecycleOwner
            binding.executePendingBindings()
        }
    }
}

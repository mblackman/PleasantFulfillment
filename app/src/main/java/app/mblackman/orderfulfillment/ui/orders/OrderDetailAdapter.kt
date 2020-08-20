package app.mblackman.orderfulfillment.ui.orders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.databinding.ListItemOrderDetailsBinding
import app.mblackman.orderfulfillment.ui.utils.ExpandState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

/**
 * An adapter for order details and related items. Helps bind the order properties with
 * view items in a collection.
 *
 * @param lifecycleOwner The lifecycle owner for the owner of this adapter.
 */
class OrderDetailAdapter(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(OrderDetailsDiffCallback) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    /**
     * Checks for differences between orders.
     */
    companion object OrderDetailsDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BindingViewHolder -> {
                val item = getItem(position) as DataItem.OrderDetailsItem
                holder.bind(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemOrderDetailsBinding.inflate(layoutInflater, parent, false)

                return BindingViewHolder(binding)
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.OrderDetailsItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    /**
     * Adds the given collection of orders to the view and creates a header for the list.
     *
     * @param list The list of orders.
     */
    fun addHeaderAndSubmitList(list: List<Order>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.OrderDetailsItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
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
        fun bind(item: DataItem.OrderDetailsItem) {
            binding.order = item.order
            binding.state = item.state

            val productSalesAdapter =
                ProductSaleAdapter(lifecycleOwner, item.order.productSales, item.state.isExpanded)
            val decorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                setDrawable(ContextCompat.getDrawable(context, R.drawable.divider)!!)
            }
            binding.productSales.adapter = productSalesAdapter
            binding.productSales.addItemDecoration(decorator)

            binding.lifecycleOwner = lifecycleOwner
            binding.executePendingBindings()
        }
    }

    /**
     * Creates a text view holder.
     */
    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            /**
             * Factory to create the view holder from a parent view group.
             *
             * @param parent The parent view for this view being created.
             * @return The created view holder.
             */
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view =
                    layoutInflater.inflate(R.layout.list_item_order_details_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}

/**
 * Decorator to contain the items stored inside the adapter's collection.
 */
sealed class DataItem {
    /**
     * Holds an order.
     *
     * @param order The order to hold.
     */
    data class OrderDetailsItem(val order: Order) : DataItem() {
        override val id = order.id
        val state = ExpandState()
    }

    /**
     * Holds the header for the list.
     */
    object Header : DataItem() {
        override val id = Int.MIN_VALUE
    }

    abstract val id: Int
}
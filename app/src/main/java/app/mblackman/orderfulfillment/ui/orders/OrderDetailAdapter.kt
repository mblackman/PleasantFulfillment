package app.mblackman.orderfulfillment.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.data.domain.Order
import app.mblackman.orderfulfillment.databinding.ListItemOrderDetailsBinding
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
class OrderDetailAdapter(private val lifecycleOwner: LifecycleOwner) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(OrderDetailsDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as DataItem.OrderDetailsItem
                holder.bind(lifecycleOwner, item.order)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
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
    class ViewHolder private constructor(private val binding: ListItemOrderDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val orderStates = HashMap<Int, OrderState>()

        /**
         * Binds the view to the given order.
         *
         * @param lifecycleOwner The lifecycle owner for this view.
         * @param item The order to bind to.
         */
        fun bind(lifecycleOwner: LifecycleOwner, item: Order) {
            binding.order = item

            if (!orderStates.containsKey(item.id)) {
                orderStates[item.id] = OrderState()
            }

            binding.state = orderStates[item.id]

            val productSalesAdapter = ProductSaleAdapter(item.productSales)
            binding.productSales.adapter = productSalesAdapter

            binding.lifecycleOwner = lifecycleOwner
            binding.executePendingBindings()
        }

        companion object {
            /**
             * Factory to create the view holder from a parent view group.
             *
             * @param parent The parent view for this view being created.
             * @return The created view holder.
             */
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemOrderDetailsBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
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
 * Checks for differences between orders.
 */
private class OrderDetailsDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
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
    }

    /**
     * Holds the header for the list.
     */
    object Header : DataItem() {
        override val id = Int.MIN_VALUE
    }

    abstract val id: Int
}

/**
 * Contains state information for an order view object.
 */
class OrderState {
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
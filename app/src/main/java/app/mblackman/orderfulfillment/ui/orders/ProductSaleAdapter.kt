package app.mblackman.orderfulfillment.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.mblackman.orderfulfillment.data.domain.ProductSale
import app.mblackman.orderfulfillment.databinding.ListItemProductSaleBinding
import app.mblackman.orderfulfillment.ui.utils.ExpandState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductSaleAdapter(
    private val lifecycleOwner: LifecycleOwner,
    items: List<ProductSale>,
    private val state: ExpandState
) : ListAdapter<ProductSale, RecyclerView.ViewHolder>(ProductSaleDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    init {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val item = getItem(position) as ProductSale
            holder.bind(lifecycleOwner, item, state)
        }
    }

    class ViewHolder private constructor(private val binding: ListItemProductSaleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the view to the given product sale.
         *
         * @param item The product sale to bind to.
         */
        fun bind(lifecycleOwner: LifecycleOwner, item: ProductSale, state: ExpandState) {
            binding.sale = item
            binding.state = state
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
                val binding = ListItemProductSaleBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Checks for differences between product sales.
 */
private class ProductSaleDiffCallback : DiffUtil.ItemCallback<ProductSale>() {
    override fun areItemsTheSame(oldItem: ProductSale, newItem: ProductSale): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ProductSale, newItem: ProductSale): Boolean {
        return oldItem.product == newItem.product
    }
}
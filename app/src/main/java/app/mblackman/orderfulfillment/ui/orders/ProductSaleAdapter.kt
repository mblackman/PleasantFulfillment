package app.mblackman.orderfulfillment.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.data.domain.ProductSale
import app.mblackman.orderfulfillment.databinding.ListItemProductSaleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A [ListAdapter] for product sales.
 *
 * @param lifecycleOwner The lifecycle owner for the owner of this adapter.
 * @param expandState A [LiveData] with the expand state of the product sale.
 */
class ProductSaleAdapter(
    private val lifecycleOwner: LifecycleOwner,
    expandState: LiveData<Boolean>
) : ListAdapter<ProductSale, RecyclerView.ViewHolder>(ProductSaleDiffCallback) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    private val productSalesState = ProductSalesState(lifecycleOwner, expandState)

    /**
     * Checks for differences between product sales.
     */
    companion object ProductSaleDiffCallback : DiffUtil.ItemCallback<ProductSale>() {
        override fun areItemsTheSame(oldItem: ProductSale, newItem: ProductSale): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProductSale, newItem: ProductSale): Boolean {
            return oldItem.product == newItem.product
        }
    }

    /**
     * Sets the items on the adapter.
     *
     * @param items The [ProductSale]s to set on the list.
     */
    fun setItems(items: List<ProductSale>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemProductSaleBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val item = getItem(position) as ProductSale
            holder.bind(item)
        }
    }

    inner class ViewHolder(private val binding: ListItemProductSaleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the view to the given product sale.
         *
         * @param item The product sale to bind to.
         */
        fun bind(item: ProductSale) {
            binding.sale = item
            binding.state = productSalesState
            binding.lifecycleOwner = lifecycleOwner
            binding.executePendingBindings()
        }
    }
}

class ProductSalesState(lifecycleOwner: LifecycleOwner, val isExpanded: LiveData<Boolean>) {
    private val _headerTextAppearance = MutableLiveData(getHeaderStyle(isExpanded.value ?: false))
    private val _itemCountTextAppearance = MutableLiveData(getCountStyle(isExpanded.value ?: false))

    init {
        isExpanded.observe(lifecycleOwner) {
            _headerTextAppearance.value = getHeaderStyle(it)
            _itemCountTextAppearance.value = getCountStyle(it)
        }
    }

    private fun getHeaderStyle(isExpanded: Boolean) =
        if (isExpanded)
            R.style.TextAppearance_MdcTypographyStyles_Headline5
        else R.style.TextAppearance_MdcTypographyStyles_Headline6

    private fun getCountStyle(isExpanded: Boolean) =
        if (isExpanded)
            R.style.TextAppearance_MdcTypographyStyles_Headline5
        else R.style.TextAppearance_MdcTypographyStyles_Body1

    val headerTextAppearance: LiveData<Int>
        get() = _headerTextAppearance

    val itemCountTextAppearance: LiveData<Int>
        get() = _itemCountTextAppearance
}
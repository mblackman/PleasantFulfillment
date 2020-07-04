package app.mblackman.orderfulfillment.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.databinding.MainFragmentBinding

class OrdersFragment : Fragment() {

    private val viewModel: OrdersViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = OrdersViewModelFactory(application)
        ViewModelProvider(this, viewModelFactory).get(OrdersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: MainFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }
}

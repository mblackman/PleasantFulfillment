package app.mblackman.orderfulfillment.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.databinding.OrderDetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private val viewModel: OrdersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: OrderDetailsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.order_details_fragment, container, false)
        val application = requireNotNull(this.activity).application

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = OrderDetailAdapter(application, this)

        viewModel.orderDetails.observe(viewLifecycleOwner, adapter::submitList)
        binding.orderDetails.adapter = adapter

        return binding.root
    }
}

package app.mblackman.orderfulfillment.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.data.database.getDatabase
import app.mblackman.orderfulfillment.data.repository.OrderRepository
import app.mblackman.orderfulfillment.data.repository.OrderRepositoryImpl
import app.mblackman.orderfulfillment.databinding.OrderDetailsFragmentBinding
import app.mblackman.orderfulfillment.ui.debug.MockStoreAdapter

class OrdersFragment : Fragment() {

    private val orderRepository: OrderRepository by lazy {
        val application = requireNotNull(this.activity).application
        //OrderRepositoryImpl.create(application)
        OrderRepositoryImpl(MockStoreAdapter(), getDatabase(application))
    }

    private val viewModel: OrdersViewModel by lazy {
        val viewModelFactory = OrdersViewModelFactory(orderRepository)
        ViewModelProvider(this, viewModelFactory).get(OrdersViewModel::class.java)
    }

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

        viewModel.orderDetails.observe(viewLifecycleOwner) {
            it.let {
                adapter.addHeaderAndSubmitList(it)
            }
        }

        adapter.addHeaderAndSubmitList(viewModel.orderDetails.value)
        binding.orderDetails.adapter = adapter

        return binding.root
    }
}

package app.mblackman.orderfulfillment.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.data.database.getDatabase
import app.mblackman.orderfulfillment.data.network.etsy.EtsyApiService
import app.mblackman.orderfulfillment.data.network.etsy.EtsyServiceGenerator
import app.mblackman.orderfulfillment.data.network.etsy.SessionManager
import app.mblackman.orderfulfillment.data.repository.OrderDetailsToOrderMapper
import app.mblackman.orderfulfillment.data.repository.OrderRepository
import app.mblackman.orderfulfillment.data.repository.OrderRepositoryImpl
import app.mblackman.orderfulfillment.data.repository.ReceiptToOrderMapper
import app.mblackman.orderfulfillment.databinding.OrderDetailsFragmentBinding
import app.mblackman.orderfulfillment.ui.debug.MockOrderRepository

class OrdersFragment : Fragment() {

    private val orderRepository: OrderRepository by lazy {
        val application = requireNotNull(this.activity).application
        val sessionManager =
            SessionManager(
                application
            )
        val etsyApiService =
            EtsyServiceGenerator(
                sessionManager
            ).createService(EtsyApiService::class.java)
        OrderRepositoryImpl(
            etsyApiService,
            getDatabase(application),
            ReceiptToOrderMapper(),
            OrderDetailsToOrderMapper()
        )
    }

    private val viewModel: OrdersViewModel by lazy {

        val viewModelFactory = OrdersViewModelFactory(MockOrderRepository())
        ViewModelProvider(this, viewModelFactory).get(OrdersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: OrderDetailsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.order_details_fragment, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = OrderDetailAdapter(this)

        viewModel.orderDetails.observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        adapter.addHeaderAndSubmitList(viewModel.orderDetails.value)
        binding.orderDetails.adapter = adapter

        return binding.root
    }
}

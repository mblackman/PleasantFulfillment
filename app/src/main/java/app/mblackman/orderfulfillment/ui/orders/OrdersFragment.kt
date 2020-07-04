package app.mblackman.orderfulfillment.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.data.database.getDatabase
import app.mblackman.orderfulfillment.data.network.EtsyApiService
import app.mblackman.orderfulfillment.data.network.EtsyServiceGenerator
import app.mblackman.orderfulfillment.data.network.SessionManager
import app.mblackman.orderfulfillment.data.repository.OrderRepository
import app.mblackman.orderfulfillment.data.repository.OrderRepositoryImpl
import app.mblackman.orderfulfillment.data.repository.ReceiptToOrderMapper
import app.mblackman.orderfulfillment.databinding.OrderDetailsFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class OrdersFragment : Fragment() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val orderRepository: OrderRepository by lazy {
        val application = requireNotNull(this.activity).application
        val sessionManager = SessionManager(application)
        val apiService = EtsyServiceGenerator(sessionManager)
            .createService(EtsyApiService::class.java)
        OrderRepositoryImpl(apiService, getDatabase(application), ReceiptToOrderMapper(), uiScope)
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

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val manager = GridLayoutManager(activity, 3)
        val adapter = OrderDetailAdapter()

        viewModel.orderDetails.observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        adapter.addHeaderAndSubmitList(viewModel.orderDetails.value)
        binding.orderDetails.layoutManager = manager
        binding.orderDetails.adapter = adapter

        return binding.root
    }
}

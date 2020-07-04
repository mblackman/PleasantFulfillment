package app.mblackman.orderfulfillment.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.data.ReceiptToOrderMapper
import app.mblackman.orderfulfillment.data.network.EtsyApiService
import app.mblackman.orderfulfillment.data.network.EtsyServiceGenerator
import app.mblackman.orderfulfillment.data.network.SessionManager
import app.mblackman.orderfulfillment.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val sessionManager = SessionManager(application)
        val apiService = EtsyServiceGenerator(sessionManager)
            .createService(EtsyApiService::class.java)
        val orderRepository = OrderRepositoryImpl(apiService, ReceiptToOrderMapper())
        val viewModelFactory = MainViewModelFactory(orderRepository)
        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
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

package app.mblackman.orderfulfillment.ui.orders

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.mblackman.orderfulfillment.LoginActivity
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.databinding.OrderDetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private val viewModel: OrdersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_login -> {
            startLoginActivity()
            true
        }
        R.id.action_refresh -> {
            viewModel.updateCurrentOrderDetails()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: OrderDetailsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.order_details_fragment, container, false)
        val application = requireNotNull(this.activity).application

        viewModel.hasValidLogin.observe(viewLifecycleOwner) {
            if (!it) {
                startLoginActivity()
            }
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = OrderDetailAdapter(application, this)

        viewModel.orderDetails.observe(viewLifecycleOwner, adapter::submitList)
        binding.orderDetails.adapter = adapter

        return binding.root
    }

    // Starts the login activity.
    private fun startLoginActivity() {
        val loginActivityIntent = Intent(this.activity, LoginActivity::class.java)
        startActivity(loginActivityIntent)
    }
}

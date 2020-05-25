package app.mblackman.orderfulfillment.ui.login

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.databinding.LoginFragmentBinding
import app.mblackman.orderfulfillment.network.SessionManager


class LoginFragment : Fragment() {

    private val sessionManager: SessionManager by lazy {
        val application = requireNotNull(this.activity).application
        SessionManager(application)
    }

    private val viewModel: LoginViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = LoginViewModelFactory(sessionManager, application)
        ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val binding: LoginFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.webview.webViewClient = LoginWebViewClient()
        binding.webview.settings.javaScriptEnabled = true

        viewModel.authorizationUrl.observe(viewLifecycleOwner, Observer {
            it?.let {
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(application, Uri.parse(it))
            }
        })

        viewModel.loginStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoginViewModel.LoginStatus.DONE -> activity?.supportFragmentManager?.popBackStack()
                LoginViewModel.LoginStatus.GETTING_AUTH_URL -> {
                }
                LoginViewModel.LoginStatus.AWAITING_OAUTH_VERIFIER -> {
                }
                LoginViewModel.LoginStatus.ERROR -> {
                }
                null -> {
                }
            }
        })

        return binding.root
    }
}

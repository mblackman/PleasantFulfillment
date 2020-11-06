package app.mblackman.orderfulfillment.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.databinding.LoginFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * The fragment that creates the login view.
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: LoginFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        loginViewModel.authorizationUrl.observe(viewLifecycleOwner, ::handleAuthorizationUrl)
        loginViewModel.loginStatus.observe(viewLifecycleOwner, ::handleLoginStatus)

        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this

        binding.imageButton.setOnClickListener {
            requireActivity().finish()
        }

        arguments?.getParcelable<Uri>("redirectUri")?.let {
            loginViewModel.handleRedirectUri(it)
        }

        return binding.root
    }

    private fun handleLoginStatus(loginStatus: LoginStatus?) {
        when (loginStatus) {
            LoginStatus.GET_AUTHORIZATION_PAGE_FAILED -> {
                Toast.makeText(context, "Failed to load authorization page", Toast.LENGTH_LONG)
                    .show()
                openLoginMessageScreen(loginStatus)
            }
            LoginStatus.LOGIN_SUCCESSFUL, LoginStatus.LOGIN_FAILED -> {
                openLoginMessageScreen(loginStatus)
            }
            else -> {
                Timber.w("Received unexpected Login Status $loginStatus")
            }
        }

        loginViewModel.handledLoginStatus()
    }

    private fun handleAuthorizationUrl(url: String?) {
        url?.let {
            val parsedUri: Uri = Uri.parse(it)
            val intent = Intent(Intent.ACTION_VIEW, parsedUri)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
            loginViewModel.handledAuthorizationUrl()
        }
    }

    private fun openLoginMessageScreen(status: LoginStatus) {
        val result = LoginResult(status)
        val action = LoginFragmentDirections.actionLoginFragmentToLoginMessage(result)
        this.findNavController().navigate(action)
    }
}

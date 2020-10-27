package app.mblackman.orderfulfillment.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.databinding.LoginFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

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

        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun handleAuthorizationUrl(url: String?) {
        if (url == null) {
            Snackbar.make(requireView(), "Unable to load authorization page.", Snackbar.LENGTH_LONG)
        } else {
            val parsedUri: Uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, parsedUri)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
        }
    }
}

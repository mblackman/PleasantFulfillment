package app.mblackman.orderfulfillment.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.mblackman.orderfulfillment.R
import app.mblackman.orderfulfillment.databinding.FragmentLoginMessageBinding

/**
 * Handles displaying login messages.
 */
class LoginMessage : Fragment() {
    private val args: LoginMessageArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentLoginMessageBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login_message,
            container,
            false
        )

        binding.result = args.loginResult

        binding.mainButton.setOnClickListener {
            when (args.loginResult.status) {
                LoginStatus.LOGIN_SUCCESSFUL -> {
                    requireActivity().finish()
                }
                else -> {
                    this.findNavController().popBackStack()
                }
            }
        }

        return binding.root
    }
}
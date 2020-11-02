package app.mblackman.orderfulfillment

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint

/**
 * Handles retrieving and maintaining login credentials for online endpoint.
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_activity)
    }

    override fun onStart() {
        super.onStart()

        intent?.data?.let {
            handleUriRedirect(it)
        }
    }

    private fun handleUriRedirect(uri: Uri) {
        val action = LoginNavigationDirections.actionGlobalLoginFragment(uri)
        Navigation.findNavController(this, R.id.login_host_nav).navigate(action)
    }
}

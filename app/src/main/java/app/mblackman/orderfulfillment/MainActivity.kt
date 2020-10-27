package app.mblackman.orderfulfillment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity that controls order fulfillment processes.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        startLoginActivity()
    }

    // Starts the login activity.
    private fun startLoginActivity() {
        val loginActivityIntent = Intent(application, LoginActivity::class.java)
        startActivity(loginActivityIntent)
    }
}

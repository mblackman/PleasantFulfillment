package app.mblackman.orderfulfillment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.mblackman.orderfulfillment.network.AuthenticationListener
import app.mblackman.orderfulfillment.network.SessionManager

class MainActivity : AppCompatActivity() {

    private val authenticationListener: AuthenticationListener = object : AuthenticationListener {
        override fun onAuthenticationFailed() {
            startLoginActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionManager = SessionManager(application)
        sessionManager.setAuthenticationListener(authenticationListener)

        setContentView(R.layout.main_activity)
    }

    private fun startLoginActivity() {
        val loginActivityIntent = Intent(application, LoginActivity::class.java)
        startActivity(loginActivityIntent)
    }
}

package app.mblackman.orderfulfillment.network

/**
 * Interface for callback methods to listen raise.
 */
interface AuthenticationListener {
    /**
     * Raised when authentication fails.
     */
    fun onAuthenticationFailed()
}
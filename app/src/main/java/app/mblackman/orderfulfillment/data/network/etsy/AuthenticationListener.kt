package app.mblackman.orderfulfillment.data.network.etsy

/**
 * Interface for callback methods to listen raise.
 */
interface AuthenticationListener {
    /**
     * Raised when authentication fails.
     */
    fun onAuthenticationFailed()
}
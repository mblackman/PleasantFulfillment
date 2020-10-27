package app.mblackman.orderfulfillment.data.network

/**
 * Represents a credential for a data source.
 */
interface Credential

/**
 * Credential data for OAuth 1.0 authorization.
 */
data class OAuthCredential(
    val consumerKey: String,
    val consumerSecret: String,
    val token: String,
    val tokenSecret: String
) : Credential
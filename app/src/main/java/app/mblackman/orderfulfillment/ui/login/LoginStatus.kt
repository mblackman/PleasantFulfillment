package app.mblackman.orderfulfillment.ui.login

/**
 * The different status conditions while logging in.
 */
enum class LoginStatus {
    NONE, LOADING, LOGIN_SUCCESSFUL, LOGIN_FAILED, GET_AUTHORIZATION_PAGE_FAILED, GET_AUTHORIZATION_PAGE_SUCCESS
}
package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.domain.User

/**
 * Gets data about users.
 */
abstract class UserRepository : BaseRepository() {

    /**
     * Gets the user for the logged in user to the application.
     *
     * @return The user for the currently logged in user.
     */
    abstract suspend fun getCurrentUserAsync(): User?
}
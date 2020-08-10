package app.mblackman.orderfulfillment.data

/**
 * Configuration information for the data.
 */
interface Configuration {

    /**
     * Gets or sets the id of the application user.
     */
    var currentUserId: Int?

    /**
     * Gets or sets the id of the current user's shop.
     */
    var currentUserShopId: Int?
}
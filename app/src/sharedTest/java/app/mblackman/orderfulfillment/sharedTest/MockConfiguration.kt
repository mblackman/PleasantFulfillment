package app.mblackman.orderfulfillment.sharedTest

import app.mblackman.orderfulfillment.data.network.etsy.Configuration

/**
 * A mock implementation of the app Configuration with public getters and setters.
 */
class MockConfiguration : Configuration {
    override var currentUserId: Int? = null

    override var currentUserShopId: Int? = null
}
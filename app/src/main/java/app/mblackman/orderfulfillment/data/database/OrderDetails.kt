package app.mblackman.orderfulfillment.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents an order.
 */
@Entity
data class OrderDetails constructor(
    @PrimaryKey
    val id: Int
)
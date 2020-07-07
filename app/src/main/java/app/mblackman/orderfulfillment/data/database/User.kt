package app.mblackman.orderfulfillment.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A user in the system.
 */
@Entity
data class User(
    @PrimaryKey
    val id: Int
)
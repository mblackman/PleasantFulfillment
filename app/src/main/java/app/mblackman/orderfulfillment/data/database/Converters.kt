package app.mblackman.orderfulfillment.data.database

import androidx.room.TypeConverter
import app.mblackman.orderfulfillment.data.common.OrderStatus
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromInt(value: Int?): OrderStatus? = value?.toEnum<OrderStatus>()

    @TypeConverter
    fun orderStatusToInt(value: OrderStatus?): Int? = value?.toInt()

    private fun <T : Enum<T>> T.toInt(): Int = this.ordinal

    private inline fun <reified T : Enum<T>> Int.toEnum(): T = enumValues<T>()[this]
}
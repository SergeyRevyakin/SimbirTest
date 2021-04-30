package ru.serg.simbirtest.data.room

import androidx.room.TypeConverter
import java.sql.Timestamp

class TimestampTypeConverter {

    @TypeConverter
    fun timestampToLong(t:Timestamp) = t.time

    @TypeConverter
    fun longToTimestamp(l:Long) = Timestamp(l)
}
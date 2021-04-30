package ru.serg.simbirtest.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.serg.simbirtest.data.model.ScheduleItem

@Database(entities = [ScheduleItem::class], version = 1)
@TypeConverters(value = [TimestampTypeConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "SimbirAppDB"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}
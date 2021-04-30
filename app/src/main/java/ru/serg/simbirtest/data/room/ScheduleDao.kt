package ru.serg.simbirtest.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.serg.simbirtest.data.model.ScheduleItem

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScheduleItem(scheduleItem: ScheduleItem)

    @Query("SELECT * FROM ScheduleItem")
    fun getAllScheduleItems():Flow<List<ScheduleItem>>

    @Query("SELECT * FROM ScheduleItem WHERE dateStart BETWEEN :dayStart AND :dayEnd")
    fun getScheduleOfDay(dayStart:Long, dayEnd:Long): Flow<List<ScheduleItem>>
}
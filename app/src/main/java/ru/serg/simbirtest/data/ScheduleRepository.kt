package ru.serg.simbirtest.data

import ru.serg.simbirtest.data.room.ScheduleDao
import ru.serg.simbirtest.data.model.ScheduleItem
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val scheduleDao: ScheduleDao
) {
    suspend fun insertScheduleItem(scheduleItem: ScheduleItem){
        scheduleDao.insertScheduleItem(scheduleItem)
    }

    fun getScheduleOfDay(dayStart:Long, dayEnd:Long) =
        scheduleDao.getScheduleOfDay(dayStart,dayEnd)
}
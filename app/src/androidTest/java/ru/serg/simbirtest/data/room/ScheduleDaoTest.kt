package ru.serg.simbirtest.data.room

import android.icu.util.Calendar
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.serg.simbirtest.data.model.ScheduleItem
import ru.serg.simbirtest.extension.Extension

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ScheduleDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var dao: ScheduleDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.scheduleDao()
    }

    @After
    fun clear() {
        database.close()
    }

    @Test
    fun insertScheduleItem() = runBlockingTest {
        val scheduleItem =
            ScheduleItem(1619647220915, 1619661620915, "todo something", "todo description", 1)
        dao.insertScheduleItem(scheduleItem)

        val allItems = dao.getAllScheduleItems().first()

        assertThat(allItems.contains(scheduleItem))
    }

    @Test
    fun getScheduleItemOfDay() = runBlockingTest {
        val calendar = Calendar.getInstance()
        val startOfDay = Extension().atStartOfDay(calendar.time)
        val endOfDay = Extension().atEndOfDay(calendar.time)

        calendar.set(Calendar.HOUR_OF_DAY, 12)
        val startTime = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 14)
        val endTime = calendar.timeInMillis

        val scheduleItemOfDay =
            ScheduleItem(startTime, endTime, "todo something", "todo description", 1)
        dao.insertScheduleItem(scheduleItemOfDay)

        val itemsOfDay = dao.getScheduleOfDay(startOfDay.time, endOfDay.time).first()

        assertThat(itemsOfDay).contains(scheduleItemOfDay)
    }

    @Test
    fun scheduleListNotContainsWrongDateEvents() = runBlockingTest {
        val calendar = Calendar.getInstance()
        val startOfDay = Extension().atStartOfDay(calendar.time)
        val endOfDay = Extension().atEndOfDay(calendar.time)

        calendar.set(Calendar.DAY_OF_MONTH, 12)
        val otherStartTime = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 18)
        val otherEndTime = calendar.timeInMillis

        val otherScheduleItem =
            ScheduleItem(otherStartTime, otherEndTime, "todo something", "todo description", 2)
        dao.insertScheduleItem(otherScheduleItem)

        val itemsOfDay = dao.getScheduleOfDay(startOfDay.time, endOfDay.time).first()

        assertThat(itemsOfDay).doesNotContain(otherScheduleItem)
    }
}
package ru.serg.simbirtest.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.serg.simbirtest.data.ScheduleRepository
import ru.serg.simbirtest.data.model.ScheduleItem
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private var _schedule = MutableSharedFlow<List<ScheduleItem>>(
            replay = 0,extraBufferCapacity=0)
    val schedule get() = _schedule.asSharedFlow()
    var date: Long=0//LocalDateTime

    fun getDaySchedule(dayStart: Long, dayEnd: Long) {
        viewModelScope.launch {
            _schedule.emitAll(scheduleRepository.getScheduleOfDay(dayStart, dayEnd))
//            _schedule.emit(scheduleRepository.getScheduleOfDay(dayStart, dayEnd).first())
//            scheduleRepository.getScheduleOfDay(dayStart, dayEnd).collectLatest {
//                _schedule.emit(it)
//            }

        }
    }

    fun saveTodoInDb(scheduleItem: ScheduleItem){
        viewModelScope.launch {
            scheduleRepository.insertScheduleItem(scheduleItem)
        }
    }
}
package ru.serg.simbirtest.ui.main

import `in`.codingstudio.calendardayevent.ToDo
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.serg.simbirtest.R
import ru.serg.simbirtest.data.model.ScheduleItem
import ru.serg.simbirtest.databinding.FragmentCalendarBinding
import ru.serg.simbirtest.extension.Extension
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private lateinit var _binding: FragmentCalendarBinding
    private val binding get() = _binding

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var selectedDay: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(
            LayoutInflater.from(requireActivity()),
            container,
            false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getCurrentDaySchedule()
        setOnClickListeners()
        observe()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getCurrentDaySchedule() {
        val t = java.util.Calendar.getInstance()
        val startOfDay = Extension().atStartOfDay(t.time)
        val endOfDay = Extension().atEndOfDay(t.time)
        viewModel.date = t.timeInMillis
        viewModel.getDaySchedule(startOfDay.time, endOfDay.time)
    }

    private fun setOnClickListeners() {
        binding.apply {
            calendarView.setOnDateChangeListener { calendarView, year, month, day ->
                val t = Calendar.getInstance()
                t.set(year, month, day)
                selectedDay = t
                viewModel.date = t.timeInMillis
                val startOfDay = Extension().atStartOfDay(t.time)
                val endOfDay = Extension().atEndOfDay(t.time)

                viewModel.getDaySchedule(startOfDay.time, endOfDay.time)
                observe()
            }

            addFabButton.setOnClickListener {
                val action =
                    CalendarFragmentDirections.actionCalendarFragmentToDetailsFragment()
                findNavController().navigate(action)
            }

        }
    }

    private fun observe() {
        lifecycleScope.launch {

            viewModel.schedule.collect { list ->
                with(binding) {
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        val sortedList = list.sortedBy {
                            it.dateStart
                        }
                        adapter = ScheduleAdapter(sortedList, this@CalendarFragment::onTodoClick)
                    }

                    val todos = ArrayList<ToDo>()


                    list.forEach { scheduleItem ->
                        val startTime = java.util.Calendar.Builder()
                            .setInstant(scheduleItem.dateStart).build()

                        val endTime = java.util.Calendar.Builder()
                            .setInstant(scheduleItem.dateFinish).build()

                        val todo = ToDo()

                        todo.topic = scheduleItem.name
                        todo.description = ""
                        todo.color = resources.getColor(R.color.green)

                        todo.startTime = startTime
                        todo.endTime = endTime

                        todos.add(todo)
                    }
                    timeline.setTodos(todos)
                }
            }
        }
    }

    private fun onTodoClick(scheduleItem: ScheduleItem) {

        val action =
            CalendarFragmentDirections.actionCalendarFragmentToDetailsFragment(scheduleItem)
        findNavController().navigate(action)
    }

}
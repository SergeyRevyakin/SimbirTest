package ru.serg.simbirtest.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import ru.serg.simbirtest.R
import ru.serg.simbirtest.data.model.ScheduleItem
import ru.serg.simbirtest.databinding.FragmentDetailsBinding
import java.util.*


class DetailsFragment : BottomSheetDialogFragment() {
    private lateinit var _binding: FragmentDetailsBinding
    private val binding get() = _binding

    private val viewModel: MainViewModel by activityViewModels()
    private val args:DetailsFragmentArgs? by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(
            LayoutInflater.from(requireActivity()),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListeners()
        val scheduleItem = args?.item
        if (scheduleItem!=null){
            with(binding){
                scheduleNameTv.setText(scheduleItem.name)
                scheduleDescriptionTv.setText(scheduleItem.description)
                val start = Calendar.Builder().setInstant(scheduleItem.dateStart).build()
                startClockTv.text = getString(R.string.time, start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE))

                val end = Calendar.Builder().setInstant(scheduleItem.dateFinish).build()
                endClockTv.text = getString(R.string.time, end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE))
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setOnClickListeners() {
        var startHour = 0
        var endHour = 0

        val startTime = Calendar.Builder().setInstant(viewModel.date).build()
        val endTime = Calendar.Builder().setInstant(viewModel.date).build()
        with(binding) {
            startTimeTv.setOnClickListener {
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Select Appointment time")
                        .build()
                picker.show(parentFragmentManager, "")

                picker.addOnPositiveButtonClickListener {
                    startHour = picker.hour
                    startClockTv.text = getString(R.string.time, picker.hour, picker.minute)
                    startTime.set(Calendar.HOUR_OF_DAY, picker.hour)
                    startTime.set(Calendar.MINUTE, picker.minute)
                }

                picker.addOnNegativeButtonClickListener {
                    dismiss()
                }
            }

            endTimeTv.setOnClickListener {
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(startHour+1)
                        .setMinute(0)
                        .setTitleText("Select Appointment time")
                        .build()
                picker.show(parentFragmentManager, "")

                picker.addOnPositiveButtonClickListener {
                    endHour = picker.hour
                    if (endHour > startHour) {
                        endClockTv.text = getString(R.string.time, picker.hour, picker.minute)
                        endTime.set(Calendar.HOUR_OF_DAY, picker.hour)
                        endTime.set(Calendar.MINUTE, picker.minute)
                    } else {
                        Toast.makeText(requireContext(), "Select correct time", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                picker.addOnNegativeButtonClickListener {
                    dismiss()
                }
            }

            saveTodoButton.setOnClickListener {
                if (endTime.timeInMillis>startTime.timeInMillis){
                    val item = ScheduleItem(
                        startTime.timeInMillis,
                        endTime.timeInMillis,
                        scheduleNameTv.text.toString(),
                        scheduleDescriptionTv.text.toString(),
                        args?.item?.id
                    )
                    viewModel.saveTodoInDb(item)
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "Select correct time", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
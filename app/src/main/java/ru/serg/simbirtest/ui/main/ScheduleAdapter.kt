package ru.serg.simbirtest.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.ConfigurationCompat
import androidx.recyclerview.widget.RecyclerView
import ru.serg.simbirtest.R
import ru.serg.simbirtest.data.model.ScheduleItem
import ru.serg.simbirtest.databinding.ItemTodoBinding
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAdapter(
    private val items: List<ScheduleItem>,
    private val onScheduleItemClick: (scheduleItem: ScheduleItem) -> Unit
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.rootView.setOnClickListener {
            onScheduleItemClick.invoke(items[position])
        }
    }

    override fun getItemCount(): Int = items.size


    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bindings = ItemTodoBinding.bind(itemView)

        fun bind(scheduleItem: ScheduleItem) {
            val startTime = Date(scheduleItem.dateStart)//, 0, OffsetDateTime.now().offset)
            val startString = SimpleDateFormat(
                "HH:mm",
                ConfigurationCompat.getLocales(itemView.resources.configuration)[0]
            ).format(startTime)

            val endTime = Date(scheduleItem.dateFinish)//, 0, OffsetDateTime.now().offset)
            val endString = SimpleDateFormat(
                "HH:mm",
                ConfigurationCompat.getLocales(itemView.resources.configuration)[0]
            ).format(endTime)
            with(bindings) {
                startTimeTv.text = startString
                endTimeTv.text = endString
                scheduleNameTv.text = scheduleItem.name
            }
        }
    }

}
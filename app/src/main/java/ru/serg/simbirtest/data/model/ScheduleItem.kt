package ru.serg.simbirtest.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.sql.Timestamp

@Entity
data class ScheduleItem(
    val dateStart: Long,//Timestamp,
    val dateFinish: Long,//Timestamp,
    val name: String,
    val description: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int?=0
):Serializable
package com.abdok.atmosphere.Data.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abdok.atmosphere.Enums.Alert

@Entity(tableName = "alerts_table")
data class AlertDTO(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startDuration: String,
    val endDuration: String,
    val selectedOption: Alert
)

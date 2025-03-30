package com.abdok.atmosphere.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abdok.atmosphere.enums.Alert

@Entity(tableName = "alerts_table")
data class AlertDTO(
    @PrimaryKey
    val id: Int,
    val startDuration: String,
    val endDuration: String,
    val selectedOption: Alert
)

package com.example.leaveapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leave_records")
data class LeaveRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: String = "2025909038",
    val name: String = "白明泽",
    val startTime: String = "",
    val endTime: String = "",
    val duration: String = "",
    val isRenew: Boolean = false,
    val reason: String = "",
    val cancelledStatus: String = "",
    val statusTag: String = ""
)
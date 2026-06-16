package com.example.leaveapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.leaveapp.LeaveApplication
import com.example.leaveapp.data.LeaveRecord
import com.example.leaveapp.data.LeaveRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LeaveViewModel(
    private val repository: LeaveRepository
) : ViewModel() {

    val allLeaves: StateFlow<List<LeaveRecord>> = repository.allLeaves
        .map { list ->
            list.sortedWith(
                compareByDescending<LeaveRecord> { it.statusTag == "待休假" }
                    .thenByDescending { it.id }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            val current = repository.allLeaves.first()
            if (current.isEmpty()) {
                val samples = listOf(
                    LeaveRecord(
                        studentId = "2025909038",
                        name = "白明泽",
                        startTime = "2025-09-21 13:00",
                        endTime = "2025-09-21 16:00",
                        duration = "0天3小时",
                        isRenew = false,
                        reason = "校外：事假",
                        cancelledStatus = "",
                        statusTag = "待休假"
                    ),
                    LeaveRecord(
                        studentId = "2025909038",
                        name = "白明泽",
                        startTime = "2025-09-19 11:00",
                        endTime = "2025-09-19 16:00",
                        duration = "0天5小时",
                        isRenew = false,
                        reason = "校外：事假",
                        cancelledStatus = "已销假",
                        statusTag = "已销假"
                    ),
                    LeaveRecord(
                        studentId = "2025909038",
                        name = "白明泽",
                        startTime = "2025-09-18 09:00",
                        endTime = "2025-09-18 12:00",
                        duration = "0天3小时",
                        isRenew = false,
                        reason = "校外：事假",
                        cancelledStatus = "已销假",
                        statusTag = "已销假"
                    )
                )
                samples.forEach { repository.insertLeave(it) }
            }
        }
    }

    fun updateLeave(record: LeaveRecord) {
        viewModelScope.launch {
            repository.updateLeave(record)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[APPLICATION_KEY] as LeaveApplication
                return LeaveViewModel(application.repository) as T
            }
        }
    }
}
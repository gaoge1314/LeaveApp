package com.example.leaveapp.data

import kotlinx.coroutines.flow.Flow

class LeaveRepository(private val leaveDao: LeaveDao) {

    val allLeaves: Flow<List<LeaveRecord>> = leaveDao.getAllLeaves()

    suspend fun updateLeave(record: LeaveRecord) {
        leaveDao.updateLeave(record)
    }

    suspend fun insertLeave(record: LeaveRecord) {
        leaveDao.insertLeave(record)
    }
}
package com.example.leaveapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LeaveDao {

    @Query("SELECT * FROM leave_records")
    fun getAllLeaves(): Flow<List<LeaveRecord>>

    @Update
    suspend fun updateLeave(record: LeaveRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeave(record: LeaveRecord)
}
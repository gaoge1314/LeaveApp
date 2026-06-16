package com.example.leaveapp

import android.app.Application
import com.example.leaveapp.data.LeaveDatabase
import com.example.leaveapp.data.LeaveRepository

class LeaveApplication : Application() {

    val database by lazy { LeaveDatabase.getInstance(this) }

    val repository by lazy { LeaveRepository(database.leaveDao()) }
}
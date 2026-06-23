package com.example.leaveapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LeaveRecord::class], version = 1)
abstract class LeaveDatabase : RoomDatabase() {

    abstract fun leaveDao(): LeaveDao

    companion object {
        @Volatile
        private var INSTANCE: LeaveDatabase? = null

        fun getInstance(context: Context): LeaveDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LeaveDatabase::class.java,
                    "leave_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
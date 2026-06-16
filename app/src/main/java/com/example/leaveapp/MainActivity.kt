package com.example.leaveapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.leaveapp.ui.LeaveListScreen
import com.example.leaveapp.ui.theme.LeaveAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LeaveAppTheme {
                LeaveListScreen()
            }
        }
    }
}
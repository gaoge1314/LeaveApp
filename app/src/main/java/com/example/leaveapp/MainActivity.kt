package com.example.leaveapp

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.leaveapp.ui.EditLeaveScreen
import com.example.leaveapp.ui.LeaveListScreen
import com.example.leaveapp.ui.LeaveViewModel
import com.example.leaveapp.ui.theme.LeaveAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 隐藏系统状态栏（仅在 Android 11+ 支持）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        setContent {
            LeaveAppTheme {
                var showEditScreen by remember { mutableStateOf(false) }
                val viewModel: LeaveViewModel = viewModel(factory = LeaveViewModel.Factory)
                val leaves by viewModel.allLeaves.collectAsState()

                if (showEditScreen && leaves.isNotEmpty()) {
                    EditLeaveScreen(
                        record = leaves.first(),
                        onSave = { updated ->
                            viewModel.updateLeave(updated)
                            showEditScreen = false
                        },
                        onBack = { showEditScreen = false }
                    )
                } else {
                    LeaveListScreen(
                        viewModel = viewModel,
                        onRenewClick = {
                            if (leaves.isNotEmpty()) {
                                showEditScreen = true
                            }
                        }
                    )
                }
            }
        }
    }
}
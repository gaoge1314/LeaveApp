package com.example.leaveapp

import android.os.Bundle
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
        setContent {
            LeaveAppTheme {
                var showEditScreen by remember { mutableStateOf(false) }
                val viewModel: LeaveViewModel = viewModel(factory = LeaveViewModel.Factory)
                val leaves by viewModel.allLeaves.collectAsState()

                // 列表始终在后台保持，编辑界面覆盖在上面
                LeaveListScreen(
                    viewModel = viewModel,
                    onRenewClick = {
                        if (leaves.isNotEmpty()) {
                            showEditScreen = true
                        }
                    }
                )

                if (showEditScreen && leaves.isNotEmpty()) {
                    EditLeaveScreen(
                        record = leaves.first(),
                        onSave = { updated ->
                            viewModel.updateLeave(updated)
                            showEditScreen = false
                        },
                        onBack = { showEditScreen = false }
                    )
                }
            }
        }
    }
}
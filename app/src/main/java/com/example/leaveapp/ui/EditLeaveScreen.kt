package com.example.leaveapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.leaveapp.data.LeaveRecord
import com.example.leaveapp.ui.theme.GradientEnd
import com.example.leaveapp.ui.theme.GradientStart
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLeaveScreen(
    record: LeaveRecord,
    onSave: (LeaveRecord) -> Unit,
    onBack: () -> Unit
) {
    var studentId by remember { mutableStateOf(record.studentId) }
    var name by remember { mutableStateOf(record.name) }
    var startTime by remember { mutableStateOf(record.startTime) }
    var endTime by remember { mutableStateOf(record.endTime) }
    var duration by remember { mutableStateOf(record.duration) }
    var isRenew by remember { mutableStateOf(record.isRenew) }
    var reason by remember { mutableStateOf(record.reason) }
    var cancelledStatus by remember { mutableStateOf(record.cancelledStatus) }
    var statusTag by remember { mutableStateOf(record.statusTag) }

    // Time picker state
    var editingTimeField by remember { mutableStateOf<TimeField?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var pendingDateMillis by remember { mutableStateOf(0L) }

    // DatePicker state
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = parseToMillis(
            if (editingTimeField == TimeField.START) startTime else endTime
        )
    )

    // TimePicker state (default to current record's time)
    val timePickerState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = 0,
        is24Hour = true
    )

    // Update TimePicker initial values when opening
    var timePickerInitialized by remember { mutableStateOf(false) }

    // DatePicker dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
                editingTimeField = null
            },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        pendingDateMillis = millis
                    }
                    showDatePicker = false
                    // Init time picker
                    val timeStr = if (editingTimeField == TimeField.START) startTime else endTime
                    val parsed = parseTimeParts(timeStr)
                    timePickerState.hour = parsed.first
                    timePickerState.minute = parsed.second
                    showTimePicker = true
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    editingTimeField = null
                }) {
                    Text("取消")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // TimePicker dialog
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = {
                showTimePicker = false
                editingTimeField = null
            },
            title = { Text("选择时间", modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center) },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TimePicker(state = timePickerState)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val dateStr = formatDateFromMillis(pendingDateMillis)
                    val timeStr = String.format(Locale.CHINA, "%02d:%02d", timePickerState.hour, timePickerState.minute)
                    val dateTimeStr = "$dateStr $timeStr"

                    when (editingTimeField) {
                        TimeField.START -> {
                            startTime = dateTimeStr
                        }
                        TimeField.END -> {
                            endTime = dateTimeStr
                        }
                        null -> {}
                    }

                    // Recalculate duration
                    duration = calculateDuration(
                        if (editingTimeField == TimeField.START) dateTimeStr else startTime,
                        if (editingTimeField == TimeField.END) dateTimeStr else endTime
                    )

                    showTimePicker = false
                    editingTimeField = null
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showTimePicker = false
                    editingTimeField = null
                }) {
                    Text("取消")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "编辑请假记录",
                        fontWeight = FontWeight(600),
                        fontSize = 17.sp,
                        color = Color(0xFF333333)
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(
                            text = "< 返回",
                            fontSize = 16.sp,
                            color = Color(0xFF333333)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {
                        onSave(
                            record.copy(
                                studentId = studentId,
                                name = name,
                                startTime = startTime,
                                endTime = endTime,
                                duration = duration,
                                isRenew = isRenew,
                                reason = reason,
                                cancelledStatus = cancelledStatus,
                                statusTag = statusTag
                            )
                        )
                    }) {
                        Text(
                            text = "保存",
                            fontSize = 16.sp,
                            color = Color(0xFF4BC2F4),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 学号
            OutlinedTextField(
                value = studentId,
                onValueChange = { studentId = it },
                label = { Text("学号") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 姓名
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("姓名") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 开始时间
            TimeFieldRow(
                label = "开始时间",
                value = startTime,
                onClick = {
                    editingTimeField = TimeField.START
                    showDatePicker = true
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 结束时间
            TimeFieldRow(
                label = "结束时间",
                value = endTime,
                onClick = {
                    editingTimeField = TimeField.END
                    showDatePicker = true
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 请假时长（只读）
            OutlinedTextField(
                value = duration,
                onValueChange = { },
                label = { Text("请假时长") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color(0xFF999999),
                    disabledBorderColor = Color(0xFFE0E0E0),
                    disabledLabelColor = Color(0xFF333333)
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 是否续假
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "是否续假",
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.width(80.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                androidx.compose.material3.Checkbox(
                    checked = isRenew,
                    onCheckedChange = { isRenew = it }
                )
                Text(
                    text = "是",
                    fontSize = 14.sp,
                    color = Color(0xFF333333)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // 请假原因
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("请假原因") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 销假状态
            OutlinedTextField(
                value = cancelledStatus,
                onValueChange = { cancelledStatus = it },
                label = { Text("销假状态") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 状态标签
            OutlinedTextField(
                value = statusTag,
                onValueChange = { statusTag = it },
                label = { Text("状态标签") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            // 底部操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.weight(1f).height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0))
                ) {
                    Text("取消", color = Color.Black, fontSize = 15.sp)
                }

                Button(
                    onClick = {
                        onSave(
                            record.copy(
                                studentId = studentId,
                                name = name,
                                startTime = startTime,
                                endTime = endTime,
                                duration = duration,
                                isRenew = isRenew,
                                reason = reason,
                                cancelledStatus = cancelledStatus,
                                statusTag = statusTag
                            )
                        )
                    },
                    modifier = Modifier.weight(1f).height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(GradientStart, GradientEnd)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("保存", color = Color.White, fontSize = 15.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TimeFieldRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF333333),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable { onClick() }
                .padding(16.dp)
        ) {
            Text(
                text = value.ifEmpty { "点击选择时间" },
                fontSize = 14.sp,
                color = if (value.isEmpty()) Color(0xFFBBBBBB) else Color(0xFF333333)
            )
        }
    }
}

private enum class TimeField { START, END }

// Utility functions

private fun parseToMillis(dateTimeStr: String): Long? {
    if (dateTimeStr.isBlank()) return null
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        sdf.parse(dateTimeStr)?.time
    } catch (e: Exception) { null }
}

private fun formatDateFromMillis(millis: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date(millis))
}

private fun parseTimeParts(dateTimeStr: String): Pair<Int, Int> {
    if (dateTimeStr.isBlank()) return Pair(0, 0)
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = sdf.parse(dateTimeStr)
        val timeSdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeStr = timeSdf.format(date!!)
        val parts = timeStr.split(":")
        Pair(parts[0].toInt(), parts[1].toInt())
    } catch (e: Exception) { Pair(0, 0) }
}

private fun calculateDuration(startTime: String, endTime: String): String {
    if (startTime.isBlank() || endTime.isBlank()) return ""
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val start = sdf.parse(startTime)?.time ?: return ""
        val end = sdf.parse(endTime)?.time ?: return ""
        val diffMs = end - start
        if (diffMs < 0) return "时间有误"
        val totalHours = diffMs / (1000 * 60 * 60)
        val days = totalHours / 24
        val hours = totalHours % 24
        "${days}天${hours}小时"
    } catch (e: Exception) { "时间有误" }
}
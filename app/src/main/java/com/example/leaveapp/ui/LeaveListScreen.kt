package com.example.leaveapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.leaveapp.data.LeaveRecord
import com.example.leaveapp.ui.theme.BadgeCancelled
import com.example.leaveapp.ui.theme.BadgePending
import com.example.leaveapp.ui.theme.BottomBarEnd
import com.example.leaveapp.ui.theme.BottomBarStart
import com.example.leaveapp.ui.theme.GradientEnd
import com.example.leaveapp.ui.theme.GradientStart
import com.example.leaveapp.ui.theme.ViewDetail
import com.example.leaveapp.ui.theme.VpnTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveListScreen(
    viewModel: LeaveViewModel = viewModel(factory = LeaveViewModel.Factory)
) {
    val leaves by viewModel.allLeaves.collectAsState()
    var editingRecord by remember { mutableStateOf<LeaveRecord?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .widthIn(max = 414.dp)
                .fillMaxSize()
                .align(Alignment.TopCenter)
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "日常请假列表",
                                fontWeight = FontWeight(600),
                                fontSize = 17.sp,
                                color = Color(0xFF333333),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        navigationIcon = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "<",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF333333),
                                    modifier = Modifier.padding(start = 24.dp, end = 30.dp)
                                )
                                Text(
                                    text = "✕",
                                    fontSize = 18.sp,
                                    color = Color(0xFF333333),
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        },
                        actions = {
                            Text(
                                text = "•••",
                                fontSize = 16.sp,
                                color = Color(0xFF333333),
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White
                        )
                    )
                },
                bottomBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(BottomBarStart, BottomBarEnd)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "提交续假",
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Top,
                ) {
                    items(leaves, key = { it.id }) { record ->
                        LeaveCard(
                            record = record,
                            onClick = { editingRecord = record }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    // last item spacing for bottom bar
                    item { Spacer(modifier = Modifier.height(12.dp)) }
                }
            }

            // VPN 提示 - 固定在底部栏的右下角
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 10.dp, bottom = 6.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(topStart = 4.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "您正在使用VPN",
                        color = VpnTextColor,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // 编辑对话框
        editingRecord?.let { record ->
            EditLeaveDialog(
                record = record,
                onDismiss = { editingRecord = null },
                onSave = { updated ->
                    viewModel.updateLeave(updated)
                    editingRecord = null
                }
            )
        }
    }
}

@Composable
private fun LeaveCard(
    record: LeaveRecord,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().graphicsLayer { clip = true }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header row: 学号 + 查看详情
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "学号",
                            fontSize = 14.sp,
                            color = Color(0xFF999999),
                            modifier = Modifier.width(70.dp)
                        )
                        Text(
                            text = record.studentId,
                            fontSize = 14.sp,
                            color = Color(0xFF333333),
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Text(
                        text = "查看详情 >",
                        fontSize = 13.sp,
                        color = ViewDetail
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                InfoRow(label = "姓名", value = record.name)
                InfoRow(label = "请假时间", value = "${record.startTime}至${record.endTime}")
                InfoRow(label = "请假时长", value = record.duration)
                InfoRow(label = "是否续假", value = if (record.isRenew) "是" else "否")
                InfoRow(label = "请假原因", value = record.reason)
                InfoRow(label = "销假状态", value = record.cancelledStatus)

                Spacer(modifier = Modifier.height(14.dp))

                // 请假条按钮
                Button(
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(120.dp)
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
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
                        Text(
                            text = "请假条",
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // 状态标签 - 右下角45°丝带标签，右半边伸出卡片外被裁切
            val badgeColor = when {
                record.statusTag.contains("待", ignoreCase = true) -> BadgePending
                else -> BadgeCancelled
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 25.dp, y = (-46).dp)
                    .width(103.dp)
                    .height(27.dp)
                    .graphicsLayer(
                        rotationZ = -45f,
                        transformOrigin = androidx.compose.ui.graphics.TransformOrigin(
                            pivotFractionX = 1f,
                            pivotFractionY = 1f
                        )
                    )
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = record.statusTag.ifEmpty { "待休假" },
                    color = Color.White,
                    fontSize = 11.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF999999),
            modifier = Modifier.width(70.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color(0xFF333333)
        )
    }
}
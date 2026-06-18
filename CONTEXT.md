# LeaveApp — 领域术语表

## 项目概述
原生 Android 请假管理应用。Kotlin + Jetpack Compose + Room 本地数据库。

## 核心概念

### 请假记录 (LeaveRecord)
一条请假申请的完整数据。包含学号、姓名、时间、时长、原因、状态等字段。

### 待休假 (Pending)
状态标签之一，表示请假申请已提交但尚未销假。在列表中置顶显示。

### 已销假 (Cancelled)
状态标签之一，表示该请假已销假。排在待休假之后。

### 提交续假 (Submit Renewal)
底部栏按钮。点击后打开全屏编辑页面，编辑 **列表中排序后的第一条记录**（始终是列表最上面那张卡片）。

### 全屏编辑页面 (EditLeaveScreen)
点击"提交续假"后打开的独立页面，用于编辑记录的所有字段。
- 可以**重新设计 UI**，以便于操作为首要目标
- 时间字段使用 Material3 DatePicker + TimePicker 选择
- 请假时长根据开始/结束时间自动计算

### 列表页 (LeaveListScreen)
主页面，外观已确定，**不可修改**（除非用户明确要求）。

### 页面导航
使用 `MainActivity` 中的 `mutableStateOf` 控制列表页和编辑页的切换，不使用 Navigation 库。
package com.example.gohealthy.alarm

import com.example.gohealthy.history.HistoryItem

interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(alarmItem: AlarmItem)
}
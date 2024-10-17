package com.example.gohealthy.alarm

import com.example.gohealthy.history.HistoryItem

interface AlarmScheduler {
    fun schedule(item: AlarmItem,historyItem: HistoryItem)
    fun cancel(alarmItem: AlarmItem)
}
package com.example.gohealthy.alarm

interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(alarmItem: AlarmItem)
}
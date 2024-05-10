package com.barisaslankan.sleeptimer.presentation.screen

data class CountDownState(
    val remainingTime : Long = 180000L,
    val initialTime : Long = 180000L,
    val isMediaMuted : Boolean = false,
    val isTimerRunning : Boolean = false
)
package com.barisaslankan.sleeptimer.presentation.timer
import com.barisaslankan.sleeptimer.presentation.screen.CountDownState
import kotlinx.coroutines.*

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CountDownTimer(
    private val intervalInMillis: Long = 1000L
) {
    private var job: Job? = null

    private val _state = MutableStateFlow(CountDownState())
    val state = _state.asStateFlow()

    fun start() {
        job = CoroutineScope(Dispatchers.Default).launch {
            var elapsedTime = 0L
            while (elapsedTime < _state.value.remainingTime) {
                delay(intervalInMillis)
                elapsedTime += intervalInMillis
                _state.update {
                    it.copy(remainingTime = it.remainingTime - elapsedTime)
                }
            }
        }
    }

    fun stop() {
        job?.cancel()
        _state.update {
            it.copy(remainingTime = _state.value.initalTime)
        }
    }
}
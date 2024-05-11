package com.barisaslankan.sleeptimer.presentation.screen

import androidx.lifecycle.ViewModel
import com.barisaslankan.sleeptimer.presentation.media.MediaManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountDownViewModel @Inject constructor(
    private val mediaManager: MediaManager
) : ViewModel() {

    private val _state = MutableStateFlow(CountDownState())
    val state = _state.asStateFlow()

    private var job: Job? = null

    private val intervalInMillis: Long = 1000L

    fun startTimer() {
        job = CoroutineScope(Dispatchers.Default).launch {
            while (intervalInMillis <= _state.value.remainingTime) {
                delay(intervalInMillis)
                _state.update {
                    it.copy(remainingTime = it.remainingTime - intervalInMillis, isTimerRunning = true)
                }
            }
            stopTimer()
            muteAllMedia()
        }
    }

    fun updateRemainingTime(initialRemainingTime : Long){
        _state.update {
            it.copy(remainingTime = initialRemainingTime)
        }
    }

    fun stopTimer() {
        job?.cancel()
        updateRemainingTime(_state.value.initialTime)
        _state.update {
            it.copy(remainingTime = _state.value.initialTime, isTimerRunning = false)
        }
    }

    fun setInitialTime(initialTime : Long){
        _state.update {
            it.copy(initialTime = initialTime)
        }
    }

    private fun muteAllMedia(){
        mediaManager.muteAllMedia()
        _state.update {
            it.copy(isMediaMuted = true)
        }
    }
}
package com.barisaslankan.sleeptimer.presentation.screen

import androidx.compose.ui.geometry.Offset
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
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt

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
            var elapsedTime = 0L
            while (elapsedTime < _state.value.remainingTime) {
                delay(intervalInMillis)
                elapsedTime += intervalInMillis
                _state.update {
                    it.copy(remainingTime = it.remainingTime - elapsedTime, isTimerRunning = true)
                }
            }
        }
    }

    fun stopTimer() {
        job?.cancel()
        _state.update {
            it.copy(remainingTime = _state.value.initialTime, isTimerRunning = false)
        }
    }

    fun setInitialTime(initialTime : Long){
        _state.update {
            it.copy(initialTime = initialTime)
        }
    }

    fun muteAllMedia(){
        mediaManager.muteAllMedia()
        _state.update {
            it.copy(isMediaMuted = true)
        }
    }

    fun calculateAngleDelta(currentPoint: Offset, initialPoint: Offset): Float {
        val deltaX = currentPoint.x - initialPoint.x
        val deltaY = currentPoint.y - initialPoint.y
        return atan2(deltaY, deltaX)
    }

    fun calculateTotalRevolutions(angleDelta: Float): Int {
        val totalRadians = angleDelta * PI.toFloat() / 180f
        val totalRevolutions = totalRadians / (2 * PI.toFloat())
        return totalRevolutions.roundToInt()
    }
}
package com.barisaslankan.sleeptimer.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.barisaslankan.sleeptimer.util.toMinutes
import java.util.concurrent.TimeUnit
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun CountDownScreen(
    countDownViewModel: CountDownViewModel = hiltViewModel()
) {
    val state = countDownViewModel.state.collectAsState()
    val radius = 120.dp
    var startAngle = 0.0
    var totalTurns = 0
    var lastPointerPosition: Offset? = null
    val touchPoint = remember { mutableStateOf(Offset(0f, -radius.value / 2)) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if(!state.value.isTimerRunning){
            CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(radius * 2),
            strokeWidth = 12.dp,
        )

        Text(
            text = "${state.value.initialTime.toMinutes()} minutes",
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(fontSize = 24.sp)
        )
         Box(
             modifier = Modifier
                 .offset {
                     val angle = atan2(touchPoint.value.y.toDouble(), touchPoint.value.x.toDouble())
                     val x = (radius.toPx() * cos(angle)).toFloat()
                     val y = (radius.toPx() * sin(angle)).toFloat()
                     IntOffset(x.roundToInt(), y.roundToInt())
                 }
                 .size(32.dp)
                 .clip(CircleShape)
                 .background(Color.Blue)
                 .pointerInput(Unit) {
                     detectDragGestures(
                         onDragStart = { lastPointerPosition = it },
                         onDrag = { change, dragAmount ->
                             change.consume()
                             touchPoint.value += dragAmount
                             val newPointerPosition = change.position
                             lastPointerPosition?.let { lastPos ->
                                 val initialAngle = atan2(lastPos.y, lastPos.x)
                                 val finalAngle = atan2(newPointerPosition.y, newPointerPosition.x)
                                 val angleChange = finalAngle - initialAngle
                                 startAngle += angleChange
                                 startAngle %= (2 * Math.PI)
                                 val clockwiseRotation = angleChange > 0
                                 if (clockwiseRotation) {
                                     totalTurns++
                                 } else {
                                     totalTurns--
                                 }

                                 val linearValue = totalTurns * 2 * Math.PI * radius.value

                                 val newInitialTime = if (clockwiseRotation) {
                                     state.value.initialTime + linearValue.toLong()
                                 } else {
                                     if(state.value.initialTime>=linearValue.toLong()){
                                         state.value.initialTime - linearValue.toLong()
                                     }else {
                                         0L
                                     }
                                 }
                                 countDownViewModel.setInitialTime(newInitialTime)
                                 countDownViewModel.updateRemainingTime(newInitialTime)
                             }
                             lastPointerPosition = newPointerPosition
                         }
                     )
                 }
            )
        }else {
            val remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(state.value.remainingTime)
            val remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(state.value.remainingTime) % 60
            val formattedTime = "%02d:%02d".format(remainingMinutes, remainingSeconds)

            Text(
                text = formattedTime,
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = 40.sp
                )
            )
        }

        Button(
            onClick = {
                if (state.value.isTimerRunning) countDownViewModel.stopTimer()
                else countDownViewModel.startTimer()
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = if (state.value.isTimerRunning) "Stop" else "Start")
        }
    }
}

@Preview
@Composable
fun CountDownScreenPreview(){
    CountDownScreen()
}
package com.example.lifecycleawaretimer.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun TimerScreen(timerViewModel: TimerViewModel) {

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                timerViewModel.getTimerDataFromPreferences()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            timerViewModel.getFormattedTime(),
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (timerViewModel.timerJob == null) timerViewModel.startTimer()
            else timerViewModel.stopTimer(isPause = true)
        }) {
            Text("Start/Pause")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            timerViewModel.stopTimer(isPause = false)
        }) {
            Text("Reset")
        }
    }
}




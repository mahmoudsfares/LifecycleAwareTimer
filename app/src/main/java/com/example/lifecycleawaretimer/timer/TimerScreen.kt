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

    // TODO 2: get lifecycle owner for composable functions
    val lifecycleOwner = LocalLifecycleOwner.current

    // TODO 3: in a disposable effect, pass the lifecycle owner to make the lifecycle aware of the event observer
    // disposable effect helps dispose of any consuming resources when the composable is disposed of
    // event observers are consuming as they always listen to the lifecycle changes, thus must be gotten rid of
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            // access the lifecycle events here
            if (event == Lifecycle.Event.ON_RESUME) {
                // TODO 9: read the saved timer state when the app resumes
                timerViewModel.getTimerDataFromPreferences()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        // TODO 4: override the disposable effect's onDispose to get rid of the observer when the composable is disposed of
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




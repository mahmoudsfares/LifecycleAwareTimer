package com.example.lifecycleawaretimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.lifecycleawaretimer.timer.TimerScreen
import com.example.lifecycleawaretimer.timer.TimerViewModel
import com.example.lifecycleawaretimer.ui.theme.LifecycleAwareTimerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val timerViewModel: TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LifecycleAwareTimerTheme {
                TimerScreen(timerViewModel)
            }
        }
    }
}

package com.example.lifecycleawaretimer.timer

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    var timerSeconds by mutableLongStateOf(70)
        private set

    var timerJob: Job? = null

    private val Context.dataStore by preferencesDataStore(name = "timer_preferences")

    fun startTimer(){
        pauseTimer()
        timerJob = viewModelScope.launch {
            while (timerSeconds > 0) {
                delay(1000L)
                decrementTimer()
            }
            clearPreferences()
        }
    }

    fun pauseTimer(){
        timerJob?.cancel()
        timerJob = null
        saveTimerData(isTimerActive = false)
    }

    fun stopTimer(){
        pauseTimer()
        timerSeconds = 70
        clearPreferences()
    }

    private fun decrementTimer() {
        if (timerSeconds > 0) {
            timerSeconds -= 1
        }
        saveTimerData(isTimerActive = true)
    }

    private val CLOSURE_TIME_KEY = longPreferencesKey("closure_time")
    private val TIMER_SECONDS_KEY = longPreferencesKey("timer_second")
    private val IS_TIMER_ACTIVE = booleanPreferencesKey("is_timer_active")

    private fun saveTimerData(isTimerActive: Boolean) {
        val currentTime = System.currentTimeMillis() / 1000
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[CLOSURE_TIME_KEY] = currentTime
                preferences[TIMER_SECONDS_KEY] = timerSeconds
                preferences[IS_TIMER_ACTIVE] = isTimerActive
            }
        }
    }

    fun getTimerData() {
        viewModelScope.launch {
            val closureTime = context.dataStore.data.map { preferences ->
                preferences[CLOSURE_TIME_KEY] ?: 0L
            }.first()
            val timerSeconds = context.dataStore.data.map { preferences ->
                preferences[TIMER_SECONDS_KEY] ?: 0L
            }.first()
            val isTimerActive = context.dataStore.data.map { preferences ->
                preferences[IS_TIMER_ACTIVE] ?: false
            }.first()
            val currentTime = System.currentTimeMillis() / 1000
            val timeDifference = currentTime - closureTime

            setCurrentTimerValue(timeDifference, timerSeconds, isTimerActive)
        }
    }

    private fun setCurrentTimerValue(timeDifference: Long, timerSeconds: Long, isTimerActive: Boolean){

        if(timerSeconds == 0L) return

        if(!isTimerActive) {
            this.timerSeconds = timerSeconds
        } else {
            val remainingTimeInSeconds = timerSeconds - timeDifference
            if(remainingTimeInSeconds <= 0) stopTimer()
            else {
                this.timerSeconds = remainingTimeInSeconds
                startTimer()
            }
        }

    }

    fun clearPreferences() {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }
}
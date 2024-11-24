package com.example.lifecycleawaretimer.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey

object Constants {

    const val TIME_PREFERENCES_KEY = "timer_preferences"

    val CLOSURE_TIME_KEY = longPreferencesKey("closure_time")
    val TIMER_SECONDS_KEY = longPreferencesKey("timer_second")
    val IS_TIMER_ACTIVE = booleanPreferencesKey("is_timer_active")
}
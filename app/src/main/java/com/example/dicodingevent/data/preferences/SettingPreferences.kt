package com.example.dicodingevent.data.preferences

import android.content.Context
import android.content.SharedPreferences

class SettingPreferences(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

    var isDarkMode: Boolean
        get() = preferences.getBoolean("dark_mode", false)
        set(value) {
            preferences.edit().putBoolean("dark_mode", value).apply()
        }
}
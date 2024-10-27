package com.example.dicodingevent.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.preferences.SettingPreferences

class ViewModelFactory(private val settingPreferences: SettingPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

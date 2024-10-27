package com.example.dicodingevent.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.preferences.SettingPreferences

class SettingsViewModel(private val settingPreferences: SettingPreferences) : ViewModel() {

    val isDarkMode: LiveData<Boolean> = MutableLiveData(settingPreferences.isDarkMode)

    fun toggleDarkMode(isChecked: Boolean) {
        settingPreferences.isDarkMode = isChecked
        (isDarkMode as MutableLiveData).value = isChecked
    }
}



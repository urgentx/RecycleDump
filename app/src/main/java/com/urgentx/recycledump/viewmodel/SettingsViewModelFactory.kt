package com.urgentx.recycledump.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.urgentx.recycledump.model.Settings
import io.reactivex.Observable


class SettingsViewModelFactory(private val settingsChange: Observable<Settings> ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingsChange) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.urgentx.recycledump.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.Observable


class SettingsViewModelFactory(private val settingsChange: Observable<Int> ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java!!)) {
            return SettingsViewModel(settingsChange) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.urgentx.recycledump.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.urgentx.recycledump.model.Collector
import com.urgentx.recycledump.model.Settings
import io.reactivex.Observable

class RDViewModelFactory(private val settingsChange: Observable<Settings>? = null,
                         private val collector: Observable<Collector>? = null) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            settingsChange?.let {
                return SettingsViewModel(it) as T
            }
        } else if (modelClass.isAssignableFrom(CreateCollectorViewModel::class.java)) {
            collector?.let {
                return CreateCollectorViewModel(it) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
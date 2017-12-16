package com.urgentx.recycledump.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.urgentx.recycledump.model.CollectorDetails
import com.urgentx.recycledump.model.Settings
import io.reactivex.Observable

class RDViewModelFactory(private val settingsChange: Observable<Settings>? = null,
                         private val details: Observable<CollectorDetails>? = null) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            settingsChange?.let {
                return SettingsViewModel(it) as T
            }
        } else if (modelClass.isAssignableFrom(CreateCollectorViewModel::class.java)) {
            details?.let {
                return CreateCollectorViewModel(it) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
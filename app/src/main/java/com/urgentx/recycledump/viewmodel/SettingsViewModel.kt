package com.urgentx.recycledump.viewmodel

import android.arch.lifecycle.ViewModel
import com.urgentx.recycledump.model.Settings
import com.urgentx.recycledump.model.api.SettingsApiInteractor
import com.urgentx.recycledump.util.firebase.FirebaseResult
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo


class SettingsViewModel//Only want errors//Only want success
(settingsChange: Observable<Settings>) : ViewModel() {

    val settings: Observable<Settings>
    val error: Observable<Int>

    private val compositeDisposable = CompositeDisposable()

    init {
        val apiInteractor = SettingsApiInteractor()
        val settingsResults = apiInteractor.retrieveSettings()
        settings = settingsResults.filter {
            //Only want success
            when (it) {
                is FirebaseResult.Success -> true
                is FirebaseResult.Error -> false
            }
        }.map {
            when (it) {
                is FirebaseResult.Success -> it.result as Settings
                is FirebaseResult.Error -> Settings(true, true, 150)
            }
        }.take(1)
        error = settingsResults.filter {
            //Only want errors
            when (it) {
                is FirebaseResult.Success -> true
                is FirebaseResult.Error -> false
            }
        }.map {
            when (it) {
                is FirebaseResult.Success -> FirebaseResult.NETWORK_ERROR
                is FirebaseResult.Error -> it.type
            }
        }
        settingsChange.subscribe { apiInteractor.updateSettings(it) }.addTo(compositeDisposable)
    }
}
package com.urgentx.recycledump.viewmodel

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.urgentx.recycledump.model.SettingsApiInteractor
import com.urgentx.recycledump.util.firebase.FirebaseResult
import io.reactivex.Observable


class SettingsViewModel : ViewModel {

    val settingsChangeSuccess: Observable<Int>

    constructor(settingsChange: Observable<Int>){
        val apiInteractor = SettingsApiInteractor()

        settingsChangeSuccess = settingsChange.filter { it == 0 }
                .flatMap { apiInteractor.updateEmailPush(true) }
                .filter{ it == FirebaseResult.SUCCESS }
                .map { 0 }

        settingsChange.subscribe { Log.d("Settings", "Switch clicked") }
    }


}
package com.urgentx.recycledump.viewmodel

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.urgentx.recycledump.debugTagCreateCollector
import com.urgentx.recycledump.model.CollectorDetails
import io.reactivex.Observable
import javax.inject.Inject

class CreateCollectorViewModel @Inject constructor(details: Observable<CollectorDetails>) : ViewModel() {init {
    details.subscribe { Log.d(debugTagCreateCollector, it.toString()) }
}

}
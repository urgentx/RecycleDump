package com.urgentx.recycledump.viewmodel

import android.arch.lifecycle.ViewModel
import com.urgentx.recycledump.model.Collector
import com.urgentx.recycledump.model.api.CreateCollectorApiInteractor
import com.urgentx.recycledump.util.firebase.FirebaseResult
import io.reactivex.Observable
import javax.inject.Inject

class CreateCollectorViewModel @Inject constructor(collector: Observable<Collector>) : ViewModel() {

    private val createCollectorApiInteractor = CreateCollectorApiInteractor()

    val createCollectorSuccess: Observable<Unit>

    init {
        val createCollectorResults = collector.flatMap {
            createCollectorApiInteractor.createCollector(it)
        }

        createCollectorSuccess = createCollectorResults.filter {
            when (it) {
                is FirebaseResult.Success -> true
                is FirebaseResult.Error -> false
            }
        }.map { Unit }
    }

}
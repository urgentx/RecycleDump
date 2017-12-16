package com.urgentx.recycledump.view

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.urgentx.recycledump.R
import com.urgentx.recycledump.model.CollectorDetails
import com.urgentx.recycledump.viewmodel.CreateCollectorViewModel
import com.urgentx.recycledump.viewmodel.RDViewModelFactory
import com.urgentx.recycledump.viewmodel.SettingsViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class CreateCollectorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_collector)



        val factory = RDViewModelFactory(details = Observable.just(CollectorDetails("Bill", "02105827452")))

        val model = ViewModelProviders.of(this, factory).get(CreateCollectorViewModel::class.java)
    }
}

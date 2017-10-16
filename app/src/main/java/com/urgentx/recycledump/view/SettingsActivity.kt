package com.urgentx.recycledump.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.RxView
import com.urgentx.recycledump.R
import com.urgentx.recycledump.viewmodel.SettingsViewModel
import com.urgentx.recycledump.viewmodel.SettingsViewModelFactory
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(settings_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = SettingsViewModelFactory(RxView.clicks(settings_mobile_sw).map{0})

        val model = ViewModelProviders.of(this, factory).get(SettingsViewModel::class.java)

    }
}

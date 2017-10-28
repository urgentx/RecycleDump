package com.urgentx.recycledump.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxSeekBar
import com.urgentx.recycledump.R
import com.urgentx.recycledump.model.Settings
import com.urgentx.recycledump.viewmodel.SettingsViewModel
import com.urgentx.recycledump.viewmodel.SettingsViewModelFactory
import io.reactivex.Observable
import io.reactivex.functions.Function3
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.concurrent.TimeUnit


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(settings_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val mobileNotifications = RxCompoundButton.checkedChanges(settings_mobile_sw)
        val emailNotifications = RxCompoundButton.checkedChanges(settings_email_sw)
        val radiusSetting = RxSeekBar.changes(settings_radius_sb).throttleWithTimeout(2, TimeUnit.SECONDS)

        val settings: Observable<Settings> = Observable.combineLatest(
                mobileNotifications,
                emailNotifications,
                radiusSetting,
                Function3 { mobile: Boolean, email: Boolean, radius: Int -> Settings(mobile, email, radius) })


        val factory = SettingsViewModelFactory(settings)

        val model = ViewModelProviders.of(this, factory).get(SettingsViewModel::class.java)

        model.settings.subscribe { t: Settings ->
            settings_mobile_sw.isChecked = t.nMobile
            settings_email_sw.isChecked = t.nEmail
            settings_radius_sb.progress = t.radius
        }

    }

}

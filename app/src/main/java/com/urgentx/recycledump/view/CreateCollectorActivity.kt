package com.urgentx.recycledump.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.borax12.materialdaterangepicker.time.RadialPickerLayout
import com.borax12.materialdaterangepicker.time.TimePickerDialog
import com.jakewharton.rxbinding2.view.clicks
import com.urgentx.recycledump.R
import com.urgentx.recycledump.model.CollectorDetails
import com.urgentx.recycledump.viewmodel.CreateCollectorViewModel
import com.urgentx.recycledump.viewmodel.RDViewModelFactory
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_create_collector.*
import java.util.*

class CreateCollectorActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_collector)

        createCollectorMondayBtn.clicks().subscribe {
            val now = Calendar.getInstance()
            val tpd = TimePickerDialog.newInstance(
                    this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false
            )
            tpd.show(fragmentManager, "Timepickerdialog")
        }

        val factory = RDViewModelFactory(details = Observable.just(CollectorDetails("Bill", "02105827452")))

        val model = ViewModelProviders.of(this, factory).get(CreateCollectorViewModel::class.java)
    }

    override fun onTimeSet(view: RadialPickerLayout?, hourOfDay: Int, minute: Int, hourOfDayEnd: Int, minuteEnd: Int) {
        Log.d("createcollector", "h: $hourOfDay")
    }
}

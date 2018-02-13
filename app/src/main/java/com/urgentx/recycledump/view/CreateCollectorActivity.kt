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
import com.urgentx.recycledump.printToLogcat
import com.urgentx.recycledump.util.helpers.hourMinToDouble
import com.urgentx.recycledump.viewmodel.CreateCollectorViewModel
import com.urgentx.recycledump.viewmodel.RDViewModelFactory
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_create_collector.*
import kotlinx.android.synthetic.main.activity_dump_info.*
import kotlinx.android.synthetic.main.content_create_collector.*
import java.util.*

class CreateCollectorActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private val compositeDisposable = CompositeDisposable()

    private var monHours = Pair<Double?, Double?>(null, null)
    private var tueHours = Pair<Double?, Double?>(null, null)
    private var wedHours = Pair<Double?, Double?>(null, null)
    private var thuHours = Pair<Double?, Double?>(null, null)
    private var friHours = Pair<Double?, Double?>(null, null)
    private var satHours = Pair<Double?, Double?>(null, null)
    private var sunHours = Pair<Double?, Double?>(null, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_collector)
        setSupportActionBar(createCollectorToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val openingHoursClicks = listOf(createCollectorMondayBtn.clicks().map { 0 },
                createCollectorTuesdayBtn.clicks().map { 1 },
                createCollectorWednesdayBtn.clicks().map { 2 },
                createCollectorThursdayBtn.clicks().map { 3 },
                createCollectorFridayBtn.clicks().map { 4 },
                createCollectorSaturdayBtn.clicks().map { 5 },
                createCollectorSundayBtn.clicks().map { 6 })

        Observable.merge(openingHoursClicks).subscribe {
            val now = Calendar.getInstance()
            val tpd = TimePickerDialog.newInstance(
                    this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false
            )
            tpd.setOnTimeSetListener { view, hourOfDay, minute, hourOfDayEnd, minuteEnd ->
                val start = hourMinToDouble(hourOfDay, minute)
                val end = hourMinToDouble(hourOfDayEnd, minuteEnd)
                val newHours = Pair(start, end)
                when (it) {
                    0 -> {
                        monHours = newHours
                        createCollectorMondayBtn.text = "$hourOfDay:$minute - $hourOfDayEnd:$minuteEnd"
                    }
                    1 -> {
                        tueHours = newHours
                        createCollectorTuesdayBtn.text = "$hourOfDay:$minute - $hourOfDayEnd:$minuteEnd"
                    }
                    2 -> {
                        wedHours = newHours
                        createCollectorWednesdayBtn.text = "$hourOfDay:$minute - $hourOfDayEnd:$minuteEnd"
                    }
                    3 -> {
                        thuHours = newHours
                        createCollectorThursdayBtn.text = "$hourOfDay:$minute - $hourOfDayEnd:$minuteEnd"
                    }
                    4 -> {
                        friHours = newHours
                        createCollectorFridayBtn.text = "$hourOfDay:$minute - $hourOfDayEnd:$minuteEnd"
                    }
                    5 -> {
                        satHours = newHours
                        createCollectorSaturdayBtn.text = "$hourOfDay:$minute - $hourOfDayEnd:$minuteEnd"
                    }
                    6 -> {
                        sunHours = newHours
                        createCollectorSundayBtn.text = "$hourOfDay:$minute - $hourOfDayEnd:$minuteEnd"
                    }
                }
            }
            tpd.show(fragmentManager, "Timepickerdialog")
        }.addTo(compositeDisposable)

        createCollectorDoneBtn.clicks().subscribe {
            printToLogcat(monHours.toString())
        }.addTo(compositeDisposable)

        val factory = RDViewModelFactory(details = Observable.just(CollectorDetails("Bill", "02105827452")))

        val model = ViewModelProviders.of(this, factory).get(CreateCollectorViewModel::class.java)
    }

    override fun onTimeSet(view: RadialPickerLayout?, hourOfDay: Int, minute: Int, hourOfDayEnd: Int, minuteEnd: Int) {
        Log.d("createcollector", "h: $hourOfDay")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            compositeDisposable.dispose()
        }
    }
}

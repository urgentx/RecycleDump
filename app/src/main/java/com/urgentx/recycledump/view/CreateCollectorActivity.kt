package com.urgentx.recycledump.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.borax12.materialdaterangepicker.time.RadialPickerLayout
import com.borax12.materialdaterangepicker.time.TimePickerDialog
import com.jakewharton.rxbinding2.view.clicks
import com.urgentx.recycledump.R
import com.urgentx.recycledump.model.Collector
import com.urgentx.recycledump.printToLogcat
import com.urgentx.recycledump.util.helpers.hourMinToDouble
import com.urgentx.recycledump.viewmodel.CreateCollectorViewModel
import com.urgentx.recycledump.viewmodel.RDViewModelFactory
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_create_collector.*
import kotlinx.android.synthetic.main.content_create_collector.*
import java.util.*

class CreateCollectorActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    private val collectorObservable = PublishSubject.create<Collector>()

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

        //Setup opening hours clicks + Pair updates.
        Observable.merge(openingHoursClicks).subscribe {
            val now = Calendar.getInstance()
            val tpd = TimePickerDialog.newInstance(
                    { _, hourOfDay, minute, hourOfDayEnd, minuteEnd ->
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
                    },
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false
            )
            tpd.show(fragmentManager, "Timepickerdialog")
        }.addTo(compositeDisposable)

        //Push Collector to VM for processing.
        createCollectorDoneBtn.clicks().subscribe {
            val collector = Collector(createCollectorName.text.toString(),
                    arrayListOf(
                            monHours, tueHours, wedHours, thuHours, friHours, satHours, sunHours
                    ).map {
                        if (it.first != null && it.second != null) {
                            return@map Pair(it.first!!, it.second!!)
                        } else {
                            return@map null
                        }
                    }.filterNotNull(),
                    createCollectorPhone.text.toString())
            collectorObservable.onNext(collector)
        }.addTo(compositeDisposable)

        //Create VM.
        val factory = RDViewModelFactory(collector = collectorObservable)
        val model = ViewModelProviders.of(this, factory).get(CreateCollectorViewModel::class.java)

        //Subscribe to VM success Observable.
        model.createCollectorSuccess.subscribe {
            Toast.makeText(this, "Collector profile created.", LENGTH_LONG).show()
            finish()
        }.addTo(compositeDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            compositeDisposable.dispose()
        }
    }
}

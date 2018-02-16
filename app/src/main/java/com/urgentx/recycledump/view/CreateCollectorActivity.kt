package com.urgentx.recycledump.view

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.borax12.materialdaterangepicker.time.TimePickerDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.jakewharton.rxbinding2.view.clicks
import com.urgentx.recycledump.R
import com.urgentx.recycledump.generateCategoryNames
import com.urgentx.recycledump.model.Collector
import com.urgentx.recycledump.util.MultiSelectSpinner
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
import kotlin.collections.HashMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.places.ui.PlacePicker
import android.R.attr.data
import android.app.Activity


private const val PLACE_PICKER_REQUEST = 1

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

        val categories = generateCategoryNames(this)

        createCollectorMultiSelectSpinner.setItems(categories)

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
            val hours = HashMap<String, Pair<Double, Double>>()
            if (monHours.first != null && monHours.second != null) hours["mon"] = Pair(monHours.first!!, monHours.second!!)
            if (tueHours.first != null && tueHours.second != null) hours["tue"] = Pair(tueHours.first!!, tueHours.second!!)
            if (wedHours.first != null && wedHours.second != null) hours["wed"] = Pair(wedHours.first!!, wedHours.second!!)
            if (thuHours.first != null && thuHours.second != null) hours["thu"] = Pair(thuHours.first!!, thuHours.second!!)
            if (friHours.first != null && friHours.second != null) hours["fri"] = Pair(friHours.first!!, friHours.second!!)
            if (satHours.first != null && satHours.second != null) hours["sat"] = Pair(satHours.first!!, satHours.second!!)
            if (sunHours.first != null && sunHours.second != null) hours["sun"] = Pair(sunHours.first!!, sunHours.second!!)
            val collector = Collector(createCollectorName.text.toString(),
                    hours,
                    createCollectorPhone.text.toString())
            collectorObservable.onNext(collector)
        }.addTo(compositeDisposable)

        createCollectorSelectLocationBtn.clicks().subscribe {
            val builder = PlacePicker.IntentBuilder()

            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(data, this)
                val toastMsg = String.format("Place: %s", place.name)
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
                //Need an API key.
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            compositeDisposable.dispose()
        }
    }
}

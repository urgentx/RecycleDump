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
import com.google.android.gms.location.places.Place
import com.jakewharton.rxbinding2.view.enabled
import com.jakewharton.rxbinding2.widget.RxAdapterView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.urgentx.recycledump.model.OpeningHours
import kotlinx.android.synthetic.main.content_recycle_info.*


private const val PLACE_PICKER_REQUEST = 1

class CreateCollectorActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    private val collectorObservable = PublishSubject.create<Collector>()

    private var openingHoursSelected: OpeningHours? = null

    private lateinit var place: Place
    private val places = PublishSubject.create<Place>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_collector)
        setSupportActionBar(createCollectorToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupUI()

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
                places.onNext(place)
                createCollectorSelectLocationBtn.text = place.address
                Toast.makeText(this, place.address, Toast.LENGTH_LONG).show()
                this.place = place
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            compositeDisposable.dispose()
        }
    }

    private fun setupUI() {
        val categories = generateCategoryNames(this)

        createCollectorMultiSelectSpinner.setItems(categories)

        val openingHoursClicks = listOf(createCollectorMondayBtn.clicks().map { 0 },
                createCollectorTuesdayBtn.clicks().map { 1 },
                createCollectorWednesdayBtn.clicks().map { 2 },
                createCollectorThursdayBtn.clicks().map { 3 },
                createCollectorFridayBtn.clicks().map { 4 },
                createCollectorSaturdayBtn.clicks().map { 5 },
                createCollectorSundayBtn.clicks().map { 6 })

        val openingHours = PublishSubject.create<OpeningHours>()

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
                                openingHours.onNext(OpeningHours(monHours = newHours))
                                createCollectorMondayBtn.text = "$hourOfDay:$minute - $hourOfDayEnd:$minuteEnd"
                            }
                            1 -> {
                                openingHours.onNext(OpeningHours(tueHours = newHours))
                                createCollectorTuesdayBtn.text = "$hourOfDay:$minute - $hourOfDayEnd:$minuteEnd"
                            }
                            2 -> {
                                openingHours.onNext(OpeningHours(wedHours = newHours))
                                createCollectorWednesdayBtn.text = "$hourOfDay:$minute - $hourOfDayEnd:$minuteEnd"
                            }
                            3 -> {
                                openingHours.onNext(OpeningHours(thuHours = newHours))
                                createCollectorThursdayBtn.text = "$hourOfDay:$minute - $hourOfDayEnd:$minuteEnd"
                            }
                            4 -> {
                                openingHours.onNext(OpeningHours(friHours = newHours))
                                createCollectorFridayBtn.text = "$hourOfDay:$minute - $hourOfDayEnd:$minuteEnd"
                            }
                            5 -> {
                                openingHours.onNext(OpeningHours(satHours = newHours))
                                createCollectorSaturdayBtn.text = "$hourOfDay:$minute - $hourOfDayEnd:$minuteEnd"
                            }
                            6 -> {
                                openingHours.onNext(OpeningHours(sunHours = newHours))
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

        openingHours.scan { prev, next ->
            prev.combine(next)
        }

        openingHours.subscribe ({
            openingHoursSelected = it
        })

        //Push Collector to VM for processing.
        createCollectorDoneBtn.clicks().subscribe {
            val collector = Collector(createCollectorName.text.toString(),
                    openingHoursSelected!!,
                    createCollectorPhone.text.toString(),
                    place.latLng.latitude,
                    place.latLng.longitude)
            collectorObservable.onNext(collector)
        }.addTo(compositeDisposable)

        createCollectorSelectLocationBtn.clicks().subscribe {
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        }.addTo(compositeDisposable)

        setupValidation(openingHours)
    }

    private fun setupValidation(openingHoursSelections: Observable<OpeningHours>) {

        val nameInputValid = RxTextView.afterTextChangeEvents(createCollectorName)
                .map {
                    it.view().text.takeIf { !it.isNullOrBlank() }
                            ?: return@map false //Check if blank
                    return@map true
                }

        val phoneNumberValid = RxTextView.afterTextChangeEvents(createCollectorPhone)
                .map {
                    it.view().text.takeIf { !it.isNullOrBlank() } ?: return@map false
                    return@map true
                }

        val openingHoursValid = openingHoursSelections.map { !it.isEmpty() }

        val placeValid = places.map {
            it.latLng != null
        }

        Observable.combineLatest(arrayOf(nameInputValid, phoneNumberValid, openingHoursValid, placeValid), {
            it[0] as Boolean && it[1] as Boolean && it[2] as Boolean && it[3] as Boolean
        }).subscribe {
            createCollectorDoneBtn.isEnabled = it
        }
    }
}

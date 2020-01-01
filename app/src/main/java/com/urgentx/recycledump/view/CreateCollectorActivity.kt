package com.urgentx.recycledump.view

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.borax12.materialdaterangepicker.time.TimePickerDialog
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.RxTextView
import com.urgentx.recycledump.R
import com.urgentx.recycledump.generateCategoryNames
import com.urgentx.recycledump.model.Collector
import com.urgentx.recycledump.model.OpeningHours
import com.urgentx.recycledump.util.helpers.hourMinToDouble
import com.urgentx.recycledump.viewmodel.CreateCollectorViewModel
import com.urgentx.recycledump.viewmodel.RDViewModelFactory
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_create_collector.*
import kotlinx.android.synthetic.main.content_create_collector.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CreateCollectorActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    private val collectorObservable = PublishSubject.create<Collector>()

    private var openingHoursSelected: OpeningHours? = null

    private lateinit var place: Place
    private val places = PublishSubject.create<Place>()

    var currentPhotoPath: String? = null

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
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            val place = PlacePicker.getPlace(this, data)
            places.onNext(place)
            createCollectorSelectLocationBtn.text = place.address
            Toast.makeText(this, place.address, Toast.LENGTH_LONG).show()
            this.place = place
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "File saved at:$currentPhotoPath", Toast.LENGTH_LONG).show()
            val drawable = Drawable.createFromPath(currentPhotoPath)
            createCollectorImage.setImageDrawable(drawable)
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

        openingHours.subscribe({
            openingHoursSelected = it
        })

        //Push Collector to VM for processing.
        createCollectorDoneBtn.clicks().subscribe {
            val collector = Collector(
                    currentPhotoPath,
                    createCollectorName.text.toString(),
                    openingHoursSelected!!,
                    createCollectorPhone.text.toString(),
                    createCollectorMultiSelectSpinner.selectedIndicies,
                    place.latLng.latitude,
                    place.latLng.longitude)
            collectorObservable.onNext(collector)
        }.addTo(compositeDisposable)

        createCollectorSelectLocationBtn.clicks().subscribe {
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        }.addTo(compositeDisposable)
        createCollectorPhotoBtn.setOnClickListener({
            dispatchTakePictureIntent()
        })
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
            return@combineLatest !it.contains(false) && createCollectorMultiSelectSpinner.selectedIndicies.isNotEmpty()
        }).subscribe {
            createCollectorDoneBtn.isEnabled = it
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            //File where photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                //Error during file creation
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this, "com.urgentx.recycledump.fileprovider",
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun createImageFile(): File {
        //Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(fileName, ".jpg", storageDir)
        //Save file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.absolutePath
        return image
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val PLACE_PICKER_REQUEST = 2
    }
}

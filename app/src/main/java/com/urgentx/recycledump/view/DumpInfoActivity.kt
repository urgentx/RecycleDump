package com.urgentx.recycledump.view

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Html
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.RxTextView
import com.urgentx.recycledump.R
import com.urgentx.recycledump.generateCategories
import com.urgentx.recycledump.presenter.RecycleInfoPresenter
import com.urgentx.recycledump.util.Item
import com.urgentx.recycledump.util.adapter.CategorySpinnerAdapter
import com.urgentx.recycledump.view.IView.IRecycleInfoView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.content_dump_info.*
import kotlinx.android.synthetic.main.content_recycle_info.*
import kotlinx.android.synthetic.main.fragment_add_place.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DumpInfoActivity : AppCompatActivity(), IRecycleInfoView {

    private var presenter: RecycleInfoPresenter? = RecycleInfoPresenter()

    private val REQUEST_IMAGE_CAPTURE = 1
    var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dump_info)
        val toolbar = findViewById(R.id.dumpInfoToolbar) as Toolbar
        setSupportActionBar(toolbar)

        setupValidation()

        dumpInfoSaveBtn.setOnClickListener({
            var item = Item()
            item.name = dumpInfoName.text.toString()
            item.type = 1 //Dump
            item.category = dumpInfoCategory.selectedItemPosition
            item.weight = Integer.parseInt(dumpInfoWeight.text.toString())
            item.volume = dumpInfoVolume.text.toString().toDouble()
            presenter!!.saveItem(item, currentPhotoPath)        })

        dumpInfoPhotoBtn.setOnClickListener({
            dispatchTakePictureIntent()
        })

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val categories = generateCategories(this)
        val adapter = CategorySpinnerAdapter(this, R.layout.category_spinner_row, R.id.categorySpinnerTitle, categories)
        dumpInfoCategory.adapter = adapter
    }

    private fun setupValidation() {
        val nameInputValid = RxTextView.afterTextChangeEvents(dumpInfoName)
                .map {
                    it.view().text.takeIf { !it.isNullOrBlank() } ?: return@map false
                    return@map true
                }

        val weightInputValid = RxTextView.afterTextChangeEvents(dumpInfoWeight)
                .map {
                    val input = it.view().text.takeIf { !it.isNullOrBlank() } ?: return@map false
                    try {
                        input.toString().toDouble()
                        return@map true
                    } catch (e: NumberFormatException) {
                        return@map false
                    }
                }

        val volumeInputValid = RxTextView.afterTextChangeEvents(dumpInfoVolume)
                .map {
                    val input = it.view().text.takeIf { !it.isNullOrBlank() } ?: return@map false
                    try { //Check if number
                        input.toString().toDouble()
                        return@map true
                    } catch (e: NumberFormatException) {
                        return@map false
                    }
                }

        //Subscribe error messages after we've typed something.
        RxTextView.afterTextChangeEvents(dumpInfoName).skipInitialValue().take(1)
                .subscribe {
                    nameInputValid.subscribe { if (!it) dumpInfoName.error = "Enter a valid name" } }

        RxTextView.afterTextChangeEvents(dumpInfoWeight).skipInitialValue().take(1)
                .subscribe { weightInputValid.subscribe { if (!it) dumpInfoWeight.error = "Enter a valid weight" } }

        RxTextView.afterTextChangeEvents(dumpInfoVolume).skipInitialValue().take(1)
                .subscribe { volumeInputValid.subscribe { if (!it) dumpInfoVolume.error = "Enter a valid volume" } }

        val allFieldsValid = Observable.combineLatest(arrayOf(nameInputValid, weightInputValid, volumeInputValid),
                {return@combineLatest (it[0] as Boolean) && (it[1] as Boolean) && (it[2] as Boolean) })

        allFieldsValid.subscribe{dumpInfoSaveBtn.isEnabled = it}
    }

    override fun onResume() {
        super.onResume()
        presenter!!.onViewAttached(this)
    }

    override fun onPause() {
        super.onPause()
        presenter!!.onViewDetached()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            presenter = null
        }
    }

    override fun itemSaved() {
        Toast.makeText(this, "Item saved.", Toast.LENGTH_LONG).show()
    }

    override fun errorOccurred() {
        Toast.makeText(this, "Database error.", Toast.LENGTH_LONG).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "File saved at:" + currentPhotoPath, Toast.LENGTH_LONG).show()
            val drawable = Drawable.createFromPath(currentPhotoPath)
            dumpInfoImage.setImageDrawable(drawable)
        }
    }

}
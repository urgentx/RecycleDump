package com.urgentx.recycledump.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.urgentx.recycledump.R
import com.urgentx.recycledump.generateCategories
import com.urgentx.recycledump.generateCategoryNames
import com.urgentx.recycledump.presenter.AddPlacePresenter
import com.urgentx.recycledump.util.MultiSelectSpinner
import com.urgentx.recycledump.util.Place
import com.urgentx.recycledump.util.adapter.CategoryListAdapter
import com.urgentx.recycledump.view.IView.IAddPlaceView
import kotlinx.android.synthetic.main.fragment_add_place.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddPlaceFragment : IAddPlaceView, DialogFragment() {

    private val REQUEST_IMAGE_CAPTURE = 1

    var currentPhotoPath: String? = null

    private var placeLat: Double = 0.0
    private var placeLong: Double = 0.0

    private var mListener: OnFragmentInteractionListener? = null

    private val presenter: AddPlacePresenter = AddPlacePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            placeLat = arguments.getDouble(ARG_PLACE_LAT)
            placeLong = arguments.getDouble(ARG_PLACE_LONG)
        }


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_add_place, container, false)

        val categories = generateCategoryNames(activity)

        val msSpinner = view.findViewById(R.id.addPlaceMultiSelectSpinner) as MultiSelectSpinner
        msSpinner.setItems(categories)

        val placeName = view.findViewById(R.id.addPlaceName) as EditText
        val placeType = view.findViewById(R.id.addPlaceType) as ToggleButton

        (view.findViewById(R.id.addPlaceSubmitBtn) as Button).setOnClickListener({
            presenter.savePlace(Place(placeName.text.toString(), placeType.isChecked.compareTo(false), ArrayList(msSpinner.selectedIndicies), placeLat, placeLong, ""), currentPhotoPath)
        })

        (view.findViewById(R.id.addPlaceCancelBtn) as Button).setOnClickListener({
            dismiss()
        })

        view.findViewById(R.id.addPlacePhotoBtn).setOnClickListener { dispatchTakePictureIntent() }

        return view

    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            //File where photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                //Error during file creation
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(activity, "com.urgentx.recycledump.fileprovider",
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
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(fileName, ".jpg", storageDir)
        //Save file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(activity, "File saved at:" + currentPhotoPath, Toast.LENGTH_LONG).show()
            val drawable = Drawable.createFromPath(currentPhotoPath)
            addPlaceImage.setImageDrawable(drawable)
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewAttached(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.onViewDetached()
    }

    override fun placeSaved() {
        dismiss()
    }

    override fun errorOccurred() {
    }

    companion object {
        private val ARG_PLACE_LAT = "param1"
        private val ARG_PLACE_LONG = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * *
         * @param param2 Parameter 2.
         * *
         * @return A new instance of fragment AddPlaceFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(placeLat: Double, placeLong: Double): AddPlaceFragment {
            val fragment = AddPlaceFragment()
            val args = Bundle()
            args.putDouble(ARG_PLACE_LAT, placeLat)
            args.putDouble(ARG_PLACE_LONG, placeLong)
            fragment.arguments = args
            return fragment
        }
    }
}

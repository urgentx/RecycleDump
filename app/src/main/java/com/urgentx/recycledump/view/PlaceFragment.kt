package com.urgentx.recycledump.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.urgentx.recycledump.R
import com.urgentx.recycledump.generateCategoryNames

class PlaceFragment :  DialogFragment() {


    private var mListener: OnFragmentInteractionListener? = null

    private var placeName = "<Place name>"
    private var placeImg = "<Image URL>"
    private var placeCategories = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            placeName = it.getString(ARG_PLACE_NAME)!!
            placeImg = it.getString(ARG_PLACE_IMG)!!
            placeCategories = it.getIntegerArrayList(ARG_PLACE_CAT)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_place, container, false)

        (view.findViewById<TextView>(R.id.placeName)).text = placeName

        Glide.with(activity!!)
                .load(placeImg)
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                .into(view.findViewById<ImageView>(R.id.placeImage))

        val categories = generateCategoryNames(context!!)
        var categoriesString = "This place will accept your:\n"

        for (position in placeCategories){
            categoriesString += "\u2022 ${categories.get(position)}\n"
        }

        (view.findViewById<TextView>(R.id.placeAccepts)).text = categoriesString

        return view

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

    companion object {
        private val ARG_PLACE_NAME = "param_1"
        private val ARG_PLACE_IMG = "param_2"
        private val ARG_PLACE_CAT = "param_3"


        // TODO: Rename and change types and number of parameters
        fun newInstance(placeName: String, placeImg: String, placeCategories: ArrayList<Int>): PlaceFragment {
            val fragment = PlaceFragment()
            val args = Bundle()
            args.putString(ARG_PLACE_NAME, placeName)
            args.putString(ARG_PLACE_IMG, placeImg)
            args.putIntegerArrayList(ARG_PLACE_CAT, placeCategories)
            fragment.arguments = args
            return fragment
        }
    }
}

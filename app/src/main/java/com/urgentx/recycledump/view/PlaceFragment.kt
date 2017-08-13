package com.urgentx.recycledump.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.urgentx.recycledump.R

class PlaceFragment :  DialogFragment() {


    private var mListener: OnFragmentInteractionListener? = null

    private var placeName: String? = null
    private var placeImg: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            placeName = arguments.getString(ARG_PLACE_NAME)
            placeImg = arguments.getString(ARG_PLACE_IMG)
        }


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_place, container, false)

        (view.findViewById(R.id.placeName) as TextView).text = placeName

        Glide.with(activity)
                .load(placeImg)
                .into(view.findViewById(R.id.placeImage) as ImageView)


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
        fun newInstance(placeName: String, placeImg: String): PlaceFragment {
            val fragment = PlaceFragment()
            val args = Bundle()
            args.putString(ARG_PLACE_NAME, placeName)
            args.putString(ARG_PLACE_IMG, placeImg)
            fragment.arguments = args
            return fragment
        }
    }
}
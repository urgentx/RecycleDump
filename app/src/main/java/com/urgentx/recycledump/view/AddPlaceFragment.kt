package com.urgentx.recycledump.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.ToggleButton
import com.urgentx.recycledump.R
import com.urgentx.recycledump.presenter.AddPlacePresenter
import com.urgentx.recycledump.util.Place
import com.urgentx.recycledump.util.adapter.CategoryListAdapter
import com.urgentx.recycledump.view.IView.IAddPlaceView

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddPlaceFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddPlaceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddPlaceFragment : IAddPlaceView, DialogFragment() {

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

        val categories = arrayListOf("Reusable goods", "Paper", "Metals", "Glass")
        val selected = ArrayList<Boolean>()
        selected.add(false)
        selected.add(false)
        selected.add(false)
        selected.add(false)

        val categoriesLv = view.findViewById(R.id.placeCategory) as ListView
        val categoriesAdapter = CategoryListAdapter(activity, R.layout.category_row, categories, selected)
        categoriesLv.adapter = categoriesAdapter

        val placeName = view.findViewById(R.id.placeName) as EditText
        val placeType = view.findViewById(R.id.placeType) as ToggleButton

        (view.findViewById(R.id.addPlaceSubmitBtn) as Button).setOnClickListener({
            presenter.savePlace(Place(placeName.text.toString(), placeType.isChecked.compareTo(false), 3, placeLat, placeLong))
        })

        (view.findViewById(R.id.addPlaceCancelBtn) as Button).setOnClickListener({
            dismiss()
        })

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
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

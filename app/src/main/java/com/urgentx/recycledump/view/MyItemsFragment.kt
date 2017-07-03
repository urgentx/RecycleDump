package com.urgentx.recycledump.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.urgentx.recycledump.R
import kotlinx.android.synthetic.main.fragment_my_items.*

class MyItemsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_my_items, container, false)



        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val values = ArrayList<String>()
        values.add("Old rag")
        values.add("Nissan Sentra")
        values.add("Big exercise ball")

        myItemsLv.adapter = ArrayAdapter(activity, R.layout.my_items_item, values)

    }

    companion object {

        fun newInstance(): MyItemsFragment {
            val fragment = MyItemsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor

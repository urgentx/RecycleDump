package com.urgentx.recycledump.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.urgentx.recycledump.R
import com.urgentx.recycledump.presenter.MyItemsPresenter
import com.urgentx.recycledump.util.Item
import com.urgentx.recycledump.util.adapter.MyItemsAdapter
import com.urgentx.recycledump.view.IView.IMyItemsView
import kotlinx.android.synthetic.main.fragment_my_items.*


class MyItemsFragment : Fragment(), IMyItemsView {


    private var presenter: MyItemsPresenter? = MyItemsPresenter()

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

        presenter?.getItems()

    }

    override fun onResume() {
        super.onResume()
        presenter?.onViewAttached(this)
    }

    override fun onPause() {
        super.onPause()
        presenter?.onViewDetached()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter = null
    }

    override fun itemsRetrieved(items: ArrayList<Item>) {
        myItemsList.setHasFixedSize(true)
        var adapter = MyItemsAdapter(activity, items, R.layout.my_items_item)
        myItemsList.adapter = adapter
        myItemsList.layoutManager = LinearLayoutManager(activity, VERTICAL, false)
    }

    override fun errorOccurred() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

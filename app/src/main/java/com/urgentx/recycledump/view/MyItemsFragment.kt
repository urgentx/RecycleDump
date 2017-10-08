package com.urgentx.recycledump.view


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.widget.RxAdapter
import com.urgentx.recycledump.R
import com.urgentx.recycledump.presenter.MyItemsPresenter
import com.urgentx.recycledump.util.Item
import com.urgentx.recycledump.util.adapter.MyItemsAdapter
import com.urgentx.recycledump.view.IView.IMyItemsView
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_my_items.*


class MyItemsFragment : Fragment(), IMyItemsView {

    private var presenter: MyItemsPresenter? = MyItemsPresenter()
    private var adapter: MyItemsAdapter? = null

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
        adapter = MyItemsAdapter(activity, ArrayList(), R.layout.my_items_item)
        adapter?.setHasStableIds(true)
        myItemsList.layoutManager = LinearLayoutManager(activity, VERTICAL, false)
        myItemsList.adapter = adapter
        presenter?.getItems()

        adapter?.deleteSubject?.subscribe {
            presenter?.deleteItem(it) }

        adapter?.checkMapSubject?.subscribe {
            val intent = Intent(activity, PlacesActivity::class.java)
            intent.putExtra(PlacesActivity.PLACE_ID_KEY, it)
            startActivity(intent)
        }
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
        adapter?.addItems(items.toTypedArray())
    }

    override fun errorOccurred() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun itemDeleted(itemID: String) {
        adapter?.removeItem(itemID)
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

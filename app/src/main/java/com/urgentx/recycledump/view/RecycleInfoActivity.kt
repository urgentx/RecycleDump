package com.urgentx.recycledump.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.urgentx.recycledump.R
import com.urgentx.recycledump.presenter.RecycleInfoPresenter
import com.urgentx.recycledump.util.Item
import com.urgentx.recycledump.util.adapter.CategorySpinnerAdapter
import com.urgentx.recycledump.view.IView.IRecycleInfoView
import kotlinx.android.synthetic.main.content_recycle_info.*

class RecycleInfoActivity : AppCompatActivity(), IRecycleInfoView {

    private var presenter: RecycleInfoPresenter? = RecycleInfoPresenter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_info)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        recycleInfoSaveBtn.setOnClickListener({
            var item = Item()
            item.name = recycleInfoName.text.toString()
            item.type = 0
            item.category = recycleInfoCategory.selectedItemPosition
            item.weight = Integer.parseInt(recycleInfoWeight.text.toString())
            item.volume = recycleInfoVolume.text.toString().toDouble()
            presenter!!.saveItem(item)
        })

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val categories = arrayOf("Paper - including newsprint; ledger paper; computer paper; corrugated cardboard; and mixed paper.",
                "Glass", "Metal - both ferrous and nonferrous, including cans; parts from abandoned vehicles; plumbing; fences; metal doors and screens; tools; machinery; and any other discarded metal objects.")
        val adapter = CategorySpinnerAdapter(this, R.layout.category_spinner_row, R.id.categorySpinnerTitle, categories)
        recycleInfoCategory.adapter = adapter
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
        Toast.makeText(this, "Item saved.", LENGTH_LONG).show()
    }

    override fun errorOccurred() {
        Toast.makeText(this, "Database error.", LENGTH_LONG).show()
    }

}

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

        val category1 = "${getString(R.string.recycle_info_category1)} - ${getString(R.string.recycle_info_category1_desc)}"
        val category2 = "${getString(R.string.recycle_info_category2)} - ${getString(R.string.recycle_info_category2_desc)}"
        val category3 = "${getString(R.string.recycle_info_category3)} - ${getString(R.string.recycle_info_category3_desc)}"
        val category4 = "${getString(R.string.recycle_info_category4)} - ${getString(R.string.recycle_info_category4_desc)}"
        val category5 = "${getString(R.string.recycle_info_category5)} - ${getString(R.string.recycle_info_category5_desc)}"
        val category6 = "${getString(R.string.recycle_info_category6)} - ${getString(R.string.recycle_info_category6_desc)}"
        val category7 = "${getString(R.string.recycle_info_category7)} - ${getString(R.string.recycle_info_category7_desc)}"
        val category8 = "${getString(R.string.recycle_info_category8)} - ${getString(R.string.recycle_info_category8_desc)}"
        val category9 = "${getString(R.string.recycle_info_category9)} - ${getString(R.string.recycle_info_category9_desc)}"
        val category10 = "${getString(R.string.recycle_info_category10)} - ${getString(R.string.recycle_info_category10_desc)}"
        val category11 = "${getString(R.string.recycle_info_category11)} - ${getString(R.string.recycle_info_category11_desc)}"
        val category12 = "${getString(R.string.recycle_info_category12)} - ${getString(R.string.recycle_info_category12_desc)}"

        val categories = arrayOf(category1,
                category2,
                category3,
                category4,
                category5,
                category6,
                category7,
                category8,
                category9,
                category10,
                category11,
                category12)
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

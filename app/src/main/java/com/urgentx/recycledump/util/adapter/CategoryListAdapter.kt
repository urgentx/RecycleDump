package com.urgentx.recycledump.util.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.RelativeLayout
import com.urgentx.recycledump.R


class CategoryListAdapter(context: Context, resource: Int, objects: ArrayList<String>, internal var selected: ArrayList<Boolean>) : ArrayAdapter<String>(context, resource, objects) {

    private var mResourceId = 0
    private val mLayoutInflater: LayoutInflater

    init {
        mResourceId = resource
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var category: CheckedTextView? = null
        if (convertView == null) { //If new item, construct it

            val view = mLayoutInflater.inflate(mResourceId, parent, false) as RelativeLayout

            category = view.findViewById(R.id.category_row_ctv) as CheckedTextView

            category.setOnClickListener({
                (category as CheckedTextView).toggle()
                selected[position] = !selected[position]
            })
            category.text = getItem(position)
            return view
        } else { //If item already exists, update its values and reuse it.
            val view = convertView as RelativeLayout

            category = view.findViewById(R.id.category_row_ctv) as CheckedTextView
            category.isChecked = selected[position]
            category.setOnClickListener({
                (category as CheckedTextView).toggle()
                selected[position] = !selected[position]
            })
            category.text = getItem(position)
            return view
        }
    }
}

package com.urgentx.recycledump.util.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.urgentx.recycledump.R

class CategorySpinnerAdapter(context: Context?, resource: Int, textViewResourceId: Int, objects: Array<out String>?) : ArrayAdapter<String>(context, resource, textViewResourceId, objects) {

    private val layoutInflater: LayoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val resourceId = resource

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var category: CheckedTextView? = null
        return if (convertView == null) { //If new item, construct it
            val view = layoutInflater.inflate(resourceId, parent, false) as LinearLayout
            (view.findViewById(R.id.categorySpinnerTitle) as TextView).text = getItem(position)
            view
        } else { //If item already exists, update its values and reuse it.
            val view = convertView as LinearLayout
            (view.findViewById(R.id.categorySpinnerTitle) as TextView).text = getItem(position)
            view
        }
    }
}
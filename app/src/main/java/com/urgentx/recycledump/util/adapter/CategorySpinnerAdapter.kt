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

        return if (convertView == null) { //If new item, construct it
            val view = layoutInflater.inflate(resourceId, parent, false) as LinearLayout
            (view.findViewById<TextView>(R.id.categorySpinnerTitle)).text = getItem(position)
            setupIcon(position, view)
            view
        } else { //If item already exists, update its values and reuse it.
            val view = convertView as LinearLayout
            (view.findViewById<TextView>(R.id.categorySpinnerTitle)).text = getItem(position)
            setupIcon(position, view)
            view
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null) { //If new item, construct it
            val view = layoutInflater.inflate(resourceId, parent, false) as LinearLayout
            (view.findViewById<TextView>(R.id.categorySpinnerTitle)).text = getItem(position)
            setupIcon(position, view)
            view
        } else { //If item already exists, update its values and reuse it.
            val view = convertView as LinearLayout
            (view.findViewById<TextView>(R.id.categorySpinnerTitle)).text = getItem(position)
            setupIcon(position, view)
            view
        }
    }

    private fun setupIcon(position: Int, view: View){
        var  icon = R.drawable.ic_reusable
        when(position){
            0 -> icon = R.drawable.ic_paper
            1 -> icon = R.drawable.ic_reusable
            2 -> icon = R.drawable.ic_metals
            3 -> icon = R.drawable.ic_glass
            4 -> icon = R.drawable.ic_textiles
            5 -> icon = R.drawable.ic_plastics
            6 -> icon = R.drawable.ic_plant
            7 -> icon = R.drawable.ic_putrescibles
            8 -> icon = R.drawable.ic_wood
            9 -> icon = R.drawable.ic_ceramics
            10 -> icon = R.drawable.ic_soils
            11 -> icon = R.drawable.ic_chemicals
        }
        (view.findViewById<ImageView>(R.id.categorySpinnerIcon)).setImageResource(icon)
    }

    override fun getCount(): Int {
        return 12
    }
}
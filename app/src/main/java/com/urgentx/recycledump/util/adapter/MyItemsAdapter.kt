package com.urgentx.recycledump.util.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.urgentx.recycledump.R
import com.urgentx.recycledump.util.Item


class MyItemsAdapter(private var context: Context, private var items: MutableList<Item>, private var itemLayout: Int) : RecyclerView.Adapter<MyItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(itemLayout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.volume.text = item.volume.toString()
        holder.weight.text = item.weight.toString()
        Glide.with(context)
                .load(item.img)
                .into(holder.image)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var image: ImageView
        var name: TextView
        var weight: TextView
        var volume: TextView

        init {
            image = view.findViewById(R.id.myItemImage) as ImageView
            name = view.findViewById(R.id.myItemName) as TextView
            weight = view.findViewById(R.id.myItemWeight) as TextView
            volume = view.findViewById(R.id.myItemVolume) as TextView
        }
    }


}
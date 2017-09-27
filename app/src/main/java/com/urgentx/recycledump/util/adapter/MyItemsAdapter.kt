package com.urgentx.recycledump.util.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.urgentx.recycledump.R
import com.urgentx.recycledump.util.Item

class MyItemsAdapter(private var context: Context, private var items: MutableList<Item>, private var itemLayout: Int) : RecyclerView.Adapter<MyItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(itemLayout, parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("myItems", "Binding VH at position $position")
        val item = items[position]
        holder.name.text = item.name
        holder.type.text = if (item.type == 0) context.getString(R.string.recyclable) else context.getString(R.string.non_recyclable)
        holder.volume.text = "${Html.fromHtml(context.getString(R.string.volume_m_cubed))}: ${item.volume}"
        holder.weight.text = "${context.getString(R.string.weight_kg)}: ${item.weight}"
        Glide.with(context)
                .load(item.img)
                .into(holder.image)

        if(holder.isExpanded){
            holder.checkMapBtn.visibility = View.VISIBLE
            holder.deleteItemBtn.visibility = View.VISIBLE
        } else {
            holder.checkMapBtn.visibility = View.GONE
            holder.deleteItemBtn.visibility = View.GONE
        }
        holder.itemLayout.setOnClickListener {
            holder.checkMapBtn.visibility = View.VISIBLE
            holder.deleteItemBtn.visibility = View.VISIBLE
            holder.checkMapBtn.setOnClickListener{}
            holder.deleteItemBtn.setOnClickListener{}
            holder.isExpanded = true
        }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.myItemImage) as ImageView
        var name: TextView = view.findViewById(R.id.myItemName) as TextView
        var type: TextView = view.findViewById(R.id.myItemType) as TextView
        var weight: TextView = view.findViewById(R.id.myItemWeight) as TextView
        var volume: TextView = view.findViewById(R.id.myItemVolume) as TextView
        var checkMapBtn: Button = view.findViewById(R.id.myItemCheckMap) as Button
        var deleteItemBtn: Button = view.findViewById(R.id.myItemDelete) as Button
        var itemLayout: RelativeLayout = view.findViewById(R.id.myItemLayout) as RelativeLayout
        var isExpanded = false
    }
}
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
import com.jakewharton.rxbinding2.view.RxView
import com.urgentx.recycledump.R
import com.urgentx.recycledump.util.Item
import io.reactivex.subjects.PublishSubject

class MyItemsAdapter(private var context: Context, private var items: ArrayList<Item>, private var itemLayout: Int) : RecyclerView.Adapter<MyItemsAdapter.ViewHolder>() {

    val deleteSubject: PublishSubject<String> = PublishSubject.create<String>() //Bridge to view for button clicks, holds Item IDs.
    val checkMapSubject: PublishSubject<Int> = PublishSubject.create<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(itemLayout, parent, false))
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

        RxView.clicks(holder.deleteItemBtn).subscribe { deleteSubject.onNext(item.id) }
        RxView.clicks(holder.checkMapBtn).subscribe { checkMapSubject.onNext(item.category) }

        if (holder.isExpanded) {
            holder.checkMapBtn.visibility = View.VISIBLE
            holder.deleteItemBtn.visibility = View.VISIBLE
        } else {
            holder.checkMapBtn.visibility = View.GONE
            holder.deleteItemBtn.visibility = View.GONE
        }
        holder.itemLayout.setOnClickListener {
            holder.checkMapBtn.visibility = View.VISIBLE
            holder.deleteItemBtn.visibility = View.VISIBLE
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

    fun addItems(items: Array<Item>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun removeItem(itemID: String) {
        items.remove(items.find { it.id == itemID })
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById<ImageView>(R.id.myItemImage)
        var name: TextView = view.findViewById<TextView>(R.id.myItemName)
        var type: TextView = view.findViewById<TextView>(R.id.myItemType)
        var weight: TextView = view.findViewById<TextView>(R.id.myItemWeight)
        var volume: TextView = view.findViewById<TextView>(R.id.myItemVolume)
        var checkMapBtn: Button = view.findViewById<Button>(R.id.myItemCheckMap)
        var deleteItemBtn: Button = view.findViewById<Button>(R.id.myItemDelete)
        var itemLayout: RelativeLayout = view.findViewById<RelativeLayout>(R.id.myItemLayout)
        var isExpanded = false
    }


}
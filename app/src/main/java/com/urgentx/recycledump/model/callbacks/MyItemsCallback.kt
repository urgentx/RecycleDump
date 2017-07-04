package com.urgentx.recycledump.model.callbacks

import com.urgentx.recycledump.util.Item

interface MyItemsCallback{

    fun itemsRetrieved(items: ArrayList<Item>)

    fun onError()
}
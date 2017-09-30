package com.urgentx.recycledump.view.IView

import com.urgentx.recycledump.util.Item

interface IMyItemsView {

    fun itemsRetrieved(items: ArrayList<Item> )

    fun errorOccurred()

    fun itemDeleted(itemID: String)

}
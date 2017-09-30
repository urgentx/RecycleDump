package com.urgentx.recycledump.model.callbacks

import com.urgentx.recycledump.util.Item

interface ItemDeletedCallback {

    fun itemDeleted(itemID: String)

    fun deleteError()
}
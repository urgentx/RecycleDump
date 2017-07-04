package com.urgentx.recycledump.presenter

import com.urgentx.recycledump.model.MyItemsApiInteractor
import com.urgentx.recycledump.model.RecycleInfoApiInteractor
import com.urgentx.recycledump.model.callbacks.MyItemsCallback
import com.urgentx.recycledump.model.callbacks.RecycleInfoCallback
import com.urgentx.recycledump.util.Item
import com.urgentx.recycledump.view.IView.IMyItemsView
import com.urgentx.recycledump.view.IView.IRecycleInfoView

class MyItemsPresenter : MyItemsCallback{

    var view: IMyItemsView? = null
    val apiInteractor: MyItemsApiInteractor = MyItemsApiInteractor()

    var items: ArrayList<Item>? = null

    var itemSaved: Boolean = false
    var error: Int = 0

    fun getItems(){
        apiInteractor.getItems(this)
    }

    private fun updateView() {
        view?.let {
            if(items != null) {
                view!!.itemsRetrieved(items!!)
                items = null
            }

            when(error){
                1 -> view!!.errorOccurred()
            }

            error = 0

        }
    }

    fun onViewAttached(view: IMyItemsView) {
        this.view = view
        updateView()
    }

    fun onViewDetached() {
        view = null
    }

    override fun itemsRetrieved(items: ArrayList<Item>) {
        this.items = items
        updateView()
    }

    override fun onError() {

    }
}
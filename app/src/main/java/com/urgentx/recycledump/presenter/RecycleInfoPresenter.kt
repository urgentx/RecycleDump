package com.urgentx.recycledump.presenter

import com.urgentx.recycledump.model.RecycleInfoApiInteractor
import com.urgentx.recycledump.model.callbacks.RecycleInfoCallback
import com.urgentx.recycledump.util.Item
import com.urgentx.recycledump.view.IView.IRecycleInfoView

class RecycleInfoPresenter : RecycleInfoCallback {

    var view: IRecycleInfoView? = null
    val apiInteractor: RecycleInfoApiInteractor = RecycleInfoApiInteractor()

    var itemSaved: Boolean = false
    var error: Int = 0

    fun saveItem(item: Item){
        apiInteractor.storeItem(this, item)
    }

    private fun updateView() {
        view?.let {
            if (itemSaved) {
                it.itemSaved()
                itemSaved = false
            }

            if(error == 1){
                it.errorOccurred()
                error = 0
            }
        }
    }

    override fun onSuccess() {
        itemSaved = true
        updateView()
    }

    override fun onError() {
        error = 1
        updateView()
    }

    fun onViewAttached(view: IRecycleInfoView) {
        this.view = view
        updateView()
    }

    fun onViewDetached() {
        view = null
    }
}
package com.urgentx.recycledump.presenter

import com.urgentx.recycledump.model.api.AddPlaceApiInteractor
import com.urgentx.recycledump.model.callbacks.BinaryCallback
import com.urgentx.recycledump.util.Place
import com.urgentx.recycledump.view.IView.IAddPlaceView

class AddPlacePresenter : BinaryCallback {

    var view: IAddPlaceView? = null
    val apiInteractor: AddPlaceApiInteractor = AddPlaceApiInteractor()

    var placeSaved: Boolean = false
    var error: Int = 0

    fun savePlace(place: Place, picUri: String?){
        apiInteractor.savePlace(place, picUri,  this)
    }

    private fun updateView() {
        view?.let {
            if(placeSaved){
                view!!.placeSaved()
            }

            when(error){
                1 -> view!!.errorOccurred()
            }

            error = 0

        }
    }

    fun onViewAttached(view: IAddPlaceView) {
        this.view = view
        updateView()
    }

    fun onViewDetached() {
        view = null
    }

    override fun onBinarySuccess() {
        placeSaved = true
        updateView()
    }

    override fun onBinaryError() {

    }


}
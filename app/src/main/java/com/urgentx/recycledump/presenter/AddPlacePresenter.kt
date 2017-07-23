package com.urgentx.recycledump.presenter

import com.urgentx.recycledump.model.AddPlaceApiInteractor
import com.urgentx.recycledump.model.PlacesApiInteractor
import com.urgentx.recycledump.model.callbacks.BinaryCallback
import com.urgentx.recycledump.model.callbacks.PlacesCallback
import com.urgentx.recycledump.util.Place
import com.urgentx.recycledump.view.IView.IAddPlaceView
import com.urgentx.recycledump.view.IView.IPlacesView

class AddPlacePresenter : BinaryCallback {

    var view: IAddPlaceView? = null
    val apiInteractor: AddPlaceApiInteractor = AddPlaceApiInteractor()

    var placeSaved: Boolean = false
    var error: Int = 0

    fun savePlace(place: Place){
        apiInteractor.savePlace(place, this)
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
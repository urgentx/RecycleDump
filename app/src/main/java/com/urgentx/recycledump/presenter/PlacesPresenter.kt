package com.urgentx.recycledump.presenter

import com.urgentx.recycledump.model.PlacesApiInteractor
import com.urgentx.recycledump.model.callbacks.BinaryCallback
import com.urgentx.recycledump.model.callbacks.PlaceCallback
import com.urgentx.recycledump.util.Place
import com.urgentx.recycledump.view.IView.IPlacesView

class PlacesPresenter : PlaceCallback {

    var view: IPlacesView? = null
    val apiInteractor: PlacesApiInteractor = PlacesApiInteractor()

    val places = ArrayList<Place>()
    var error: Int = 0

    fun retrievePlaces(latitude: Double, longitude: Double){
        apiInteractor.retrievePlaces(latitude, longitude, this)
    }

    private fun updateView() {
        view?.let {
            if(places.isNotEmpty()){
                (view as IPlacesView).placesRetrieved(places)
                places.clear()
            }

            when(error){
                1 -> view!!.errorOccurred()
            }

            error = 0

        }
    }

    fun onViewAttached(view: IPlacesView) {
        this.view = view
        updateView()
    }

    fun onViewDetached() {
        view = null
    }

    override fun placeRetrieved(place: Place) {
        places.add(place)
        updateView()
    }

    override fun onError() {

    }
}
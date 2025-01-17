package com.urgentx.recycledump.presenter

import com.urgentx.recycledump.model.api.PlacesApiInteractor
import com.urgentx.recycledump.model.callbacks.PlaceCallback
import com.urgentx.recycledump.util.Place
import com.urgentx.recycledump.view.IView.IPlacesView

class PlacesPresenter : PlaceCallback {

    var view: IPlacesView? = null
    val apiInteractor: PlacesApiInteractor = PlacesApiInteractor()

    var places: ArrayList<Place>? = null
    var error: Int = 0

    fun retrievePlaces(latitude: Double, longitude: Double) {
        apiInteractor.retrievePlaces(latitude, longitude, this)
        apiInteractor.updateUserLocation(latitude, longitude, {}, {})
    }

    private fun updateView() {
        view?.let {
            places?.let {
                (view as IPlacesView).placesRetrieved(places!!)
                places = null
            }

            when (error) {
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
        view?.let {
            it.placeRetrieved(place)
            return
        }
        places?.add(place)
    }

    override fun onError() {

    }
}
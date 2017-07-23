package com.urgentx.recycledump.view.IView

import com.urgentx.recycledump.util.Place
interface IPlacesView {

    fun placesRetrieved(places: ArrayList<Place>)

    fun errorOccurred()

}
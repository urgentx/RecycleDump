package com.urgentx.recycledump.model.callbacks

import com.urgentx.recycledump.util.Place

interface PlacesCallback{

    fun placesRetrieved(places: ArrayList<Place>)

    fun onError()
}
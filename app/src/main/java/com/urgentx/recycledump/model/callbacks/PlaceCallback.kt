package com.urgentx.recycledump.model.callbacks

import com.urgentx.recycledump.util.Place

interface PlaceCallback {

    fun placesRetrieved(places: ArrayList<Place>)

    fun onError()
}
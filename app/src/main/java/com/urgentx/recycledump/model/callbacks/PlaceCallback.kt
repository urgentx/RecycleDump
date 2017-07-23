package com.urgentx.recycledump.model.callbacks

import com.urgentx.recycledump.util.Place

interface PlaceCallback {

    fun placeRetrieved(place: Place)

    fun onError()
}
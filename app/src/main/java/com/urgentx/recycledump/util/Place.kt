package com.urgentx.recycledump.util

class Place (var name: String, var type: Int, var category: Int, var lat: Double, var long: Double, var img: String) {    //0 = recycle, 2 = dump

    constructor() : this("", 0, 0, 0.0, 0.0, "")
}
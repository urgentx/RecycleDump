package com.urgentx.recycledump.util

class Item (var img: String, var name: String, var type: Int, var category: Int, var weight: Int, var volume: Double) {    //0 = recycle, 2 = dump

    var id: String = ""

    constructor() : this("","", 0, 0, 0, 0.0)
}
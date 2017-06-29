package com.urgentx.recycledump.util

import java.util.ArrayList

class Item (name: String, type: Int, category: Int, weight: Int,
            volume: Double) {    //0 = recycle, 2 = dump

    var name: String = ""
    var type: Int = 0
    var category: Int = 0
    var weight : Int = 0
    var volume: Double = 0.0
    var users: List<String> = ArrayList()

    init {
        this.name = name
        this.type = type
        this.category = category
        this.weight = weight
        this.volume = volume
    }

}
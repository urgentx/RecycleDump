package com.urgentx.recycledump.util

import java.util.*

class Item (var img: String, var name: String, var type: Int, var category: Int, var weight: Int, var volume: Double) {    //0 = recycle, 2 = dump

    constructor() : this("","", 0, 0, 0, 0.0)

    var users: List<String> = ArrayList()
}
package com.urgentx.recycledump.util

import java.util.*

class Item  {    //0 = recycle, 2 = dump

    var name: String = ""
    var type: Int = 0
    var category: Int = 0
    var weight : Int = 0
    var volume: Double = 0.0
    var users: List<String> = ArrayList()
}
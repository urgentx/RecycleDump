package com.urgentx.recycledump.util.helpers

fun hourMinToDouble(hours: Int, minutes: Int): Double {
    return hours.toDouble() + minutes.toDouble()/60
}
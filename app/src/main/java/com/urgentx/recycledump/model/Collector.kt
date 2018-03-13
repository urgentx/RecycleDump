package com.urgentx.recycledump.model

data class Collector (val name: String, val hours: Map<String, Pair<Double, Double>>, val phone: String, val lat: Double, val long: Double) {

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["name"] = name
        result["hours"] = hours
        result["phone"] = phone
        result["type"] = 2
        result["lat"] = lat
        result["long"] = long
        return result
    }
}
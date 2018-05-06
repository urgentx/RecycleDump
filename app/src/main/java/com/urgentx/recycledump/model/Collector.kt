package com.urgentx.recycledump.model

data class Collector (val photoPath: String?, val name: String, val hours: OpeningHours, val phone: String, val categories: List<Int>,
                      val lat: Double, val long: Double) {

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
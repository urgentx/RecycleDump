package com.urgentx.recycledump.model

data class Collector (val name: String, val hours: List<Pair<Double, Double>>, val phone: String) {

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["name"] = name
        result["hours"] = hours
        result["phone"] = phone
        return result
    }
}
package com.urgentx.recycledump.model

data class OpeningHours(val monHours: Pair<Double, Double>? = null,
                        val tueHours: Pair<Double, Double>? = null,
                        val wedHours: Pair<Double, Double>? = null,
                        val thuHours: Pair<Double, Double>? = null,
                        val friHours: Pair<Double, Double>? = null,
                        val satHours: Pair<Double, Double>? = null,
                        val sunHours: Pair<Double, Double>? = null) {


    /**
     * Takes all values of this OpeningHours and overwrites them with corresponding values of passed OpeningHours if present.
     */
    fun combine(openingHours: OpeningHours): OpeningHours {
        return this.copy(monHours = openingHours.monHours ?: monHours,
                tueHours = openingHours.tueHours ?: tueHours,
                wedHours = openingHours.wedHours ?: wedHours,
                thuHours = openingHours.thuHours ?: thuHours,
                friHours = openingHours.friHours ?: friHours,
                satHours = openingHours.satHours ?: satHours,
                sunHours = openingHours.sunHours ?: sunHours)
    }

    //TODO: Avoid this creating an 'empty' field when writing to DB.
    fun isEmpty(): Boolean {
        return (monHours != null &&
                tueHours != null &&
                wedHours != null &&
                thuHours != null &&
                friHours != null &&
                satHours != null &&
                sunHours != null)
    }
}
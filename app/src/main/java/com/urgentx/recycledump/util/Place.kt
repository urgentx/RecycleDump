package com.urgentx.recycledump.util

import android.os.Parcel
import android.os.Parcelable

class Place : Parcelable {

    var name: String = "Undefined place"
    var type: Int = 0
    var category: Int = 0
    var lat: Double = 0.0
    var long: Double = 0.0
    var img: String = ""

   constructor(name: String, type: Int, category: Int, lat: Double, long: Double, img: String){
       this.name = name
       this.type = type
       this.category = category
       this.lat = lat
       this.long = long
       this.img = img
   }

    constructor(){
        this.name = "Undefined place"
        this.type = 0
        this.category = 0
        this.lat = 0.0
        this.long = 0.0
        this.img = ""
    }
    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        type = parcel.readValue(Int::class.java.classLoader) as Int
        category = parcel.readValue(Int::class.java.classLoader) as Int
        lat = parcel.readValue(Double::class.java.classLoader) as Double
        long = parcel.readValue(Double::class.java.classLoader) as Double
        img = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeValue(type)
        parcel.writeValue(category)
        parcel.writeValue(lat)
        parcel.writeValue(long)
        parcel.writeString(img)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Place> {
        override fun createFromParcel(parcel: Parcel): Place {
            return Place(parcel)
        }

        override fun newArray(size: Int): Array<Place?> {
            return arrayOfNulls(size)
        }
    }
}
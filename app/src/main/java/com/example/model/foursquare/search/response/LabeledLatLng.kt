package com.example.model.foursquare.search.response

import com.example.model.GsonSerializable

data class LabeledLatLng (
    val label: String? = null,
    val lat: Double? = null,
    val lng: Double? = null

) : GsonSerializable {

    override fun toString(): String {
        return "LabeledLatLng(label=$label, lat=$lat, lng=$lng)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LabeledLatLng

        if (label != other.label) return false
        if (lat != other.lat) return false
        if (lng != other.lng) return false

        return true
    }

    override fun hashCode(): Int {
        var result = label?.hashCode() ?: 0
        result = 31 * result + (lat?.hashCode() ?: 0)
        result = 31 * result + (lng?.hashCode() ?: 0)
        return result
    }
}
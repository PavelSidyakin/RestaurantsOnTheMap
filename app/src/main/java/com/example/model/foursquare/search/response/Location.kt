package com.example.model.foursquare.search.response

import com.example.model.GsonSerializable

data class Location (
    val address: String? = null,
    val crossStreet: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val labeledLatLngs: List<LabeledLatLng>? = null,
    val distance: Int? = null,
    val postalCode: String? = null,
    val cc: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val formattedAddress: List<String>? = null

) : GsonSerializable {


    override fun toString(): String {
        return "Location(address=$address, crossStreet=$crossStreet, lat=$lat, lng=$lng, labeledLatLngs=$labeledLatLngs, distance=$distance, postalCode=$postalCode, cc=$cc, city=$city, state=$state, country=$country, formattedAddress=$formattedAddress)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Location

        if (address != other.address) return false
        if (crossStreet != other.crossStreet) return false
        if (lat != other.lat) return false
        if (lng != other.lng) return false
        if (labeledLatLngs != other.labeledLatLngs) return false
        if (distance != other.distance) return false
        if (postalCode != other.postalCode) return false
        if (cc != other.cc) return false
        if (city != other.city) return false
        if (state != other.state) return false
        if (country != other.country) return false
        if (formattedAddress != other.formattedAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = address?.hashCode() ?: 0
        result = 31 * result + (crossStreet?.hashCode() ?: 0)
        result = 31 * result + (lat?.hashCode() ?: 0)
        result = 31 * result + (lng?.hashCode() ?: 0)
        result = 31 * result + (labeledLatLngs?.hashCode() ?: 0)
        result = 31 * result + (distance ?: 0)
        result = 31 * result + (postalCode?.hashCode() ?: 0)
        result = 31 * result + (cc?.hashCode() ?: 0)
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (state?.hashCode() ?: 0)
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + (formattedAddress?.hashCode() ?: 0)
        return result
    }
}
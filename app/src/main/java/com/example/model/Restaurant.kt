package com.example.model

data class Restaurant (
    val id: String,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double


) {

    override fun toString(): String {
        return "Restaurant(id='$id', name='$name', address='$address', lat=$lat, lng=$lng)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Restaurant

        if (id != other.id) return false
        if (name != other.name) return false
        if (address != other.address) return false
        if (lat != other.lat) return false
        if (lng != other.lng) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + lat.hashCode()
        result = 31 * result + lng.hashCode()
        return result
    }
}
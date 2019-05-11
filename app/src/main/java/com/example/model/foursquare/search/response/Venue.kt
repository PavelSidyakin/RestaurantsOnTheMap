package com.example.model.foursquare.search.response

import com.example.model.GsonSerializable

data class Venue (
    val id: String? = null,
    val name: String? = null,
    val location: Location? = null,
    val categories: List<Category>? = null,
    val venuePage: VenuePage? = null

) : GsonSerializable {


    override fun toString(): String {
        return "Venue(id=$id, name=$name, location=$location, categories=$categories, venuePage=$venuePage)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Venue

        if (id != other.id) return false
        if (name != other.name) return false
        if (location != other.location) return false
        if (categories != other.categories) return false
        if (venuePage != other.venuePage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + (categories?.hashCode() ?: 0)
        result = 31 * result + (venuePage?.hashCode() ?: 0)
        return result
    }
}
package com.example.model.foursquare.search.response

import com.example.model.GsonSerializable

data class Response (
    val venues: List<Venue>? = null
) : GsonSerializable {


    override fun toString(): String {
        return "Response(venues=$venues)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Response

        if (venues != other.venues) return false

        return true
    }

    override fun hashCode(): Int {
        return venues?.hashCode() ?: 0
    }
}
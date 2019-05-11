package com.example.model.foursquare.details.response

import com.example.model.GsonSerializable

data class Response (
    val venue: Venue? = null
) : GsonSerializable {

    override fun toString(): String {
        return "Response(venues=$venue)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Response

        if (venue != other.venue) return false

        return true
    }

    override fun hashCode(): Int {
        return venue?.hashCode() ?: 0
    }
}
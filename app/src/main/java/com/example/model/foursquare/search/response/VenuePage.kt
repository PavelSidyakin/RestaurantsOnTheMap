package com.example.model.foursquare.search.response

import com.example.model.GsonSerializable

data class VenuePage (
    val id: String? = null
) : GsonSerializable {


    override fun toString(): String {
        return "VenuePage(id=$id)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VenuePage

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
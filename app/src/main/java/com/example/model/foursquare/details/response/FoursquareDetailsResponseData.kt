package com.example.model.foursquare.details.response

import com.example.model.GsonSerializable
import com.example.model.foursquare.Meta

data class FoursquareDetailsResponseData (
    val meta: Meta? = null,
    val response: Response? = null
) : GsonSerializable {

    override fun toString(): String {
        return "FoursquareSearchResponseData(meta=$meta, response=$response)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FoursquareDetailsResponseData

        if (meta != other.meta) return false
        if (response != other.response) return false

        return true
    }

    override fun hashCode(): Int {
        var result = meta?.hashCode() ?: 0
        result = 31 * result + (response?.hashCode() ?: 0)
        return result
    }
}
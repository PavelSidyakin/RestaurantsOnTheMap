package com.example.model.foursquare.search.response

import com.example.model.GsonSerializable

data class Icon (
    val prefix: String? = null,
    val suffix: String? = null
) : GsonSerializable {

    override fun toString(): String {
        return "Icon(prefix=$prefix, suffix=$suffix)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Icon

        if (prefix != other.prefix) return false
        if (suffix != other.suffix) return false

        return true
    }

    override fun hashCode(): Int {
        var result = prefix?.hashCode() ?: 0
        result = 31 * result + (suffix?.hashCode() ?: 0)
        return result
    }
}
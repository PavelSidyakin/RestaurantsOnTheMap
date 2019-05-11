package com.example.model.foursquare

import com.example.model.GsonSerializable

data class Meta (
    val code: Int? = null,
    val requestId: String? = null
) : GsonSerializable {

    override fun toString(): String {
        return "Meta(code=$code, requestId=$requestId)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Meta

        if (code != other.code) return false
        if (requestId != other.requestId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code ?: 0
        result = 31 * result + (requestId?.hashCode() ?: 0)
        return result
    }
}
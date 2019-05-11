package com.example.domain

import java.lang.RuntimeException

class PlacesException(placesErrorCode: PlacesErrorCode) : RuntimeException(placesErrorCode.name) {
    val errorCode = placesErrorCode

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlacesException

        if (errorCode != other.errorCode) return false

        return true
    }

    override fun hashCode(): Int {
        return errorCode.hashCode()
    }
}
package com.example.model.foursquare.details

import com.example.model.foursquare.details.response.FoursquareDetailsResponseData

data class FoursquareDetailsResult (
    val code: FoursquareDetailsResultCode,
    val foursquareDetailsResponseData: FoursquareDetailsResponseData?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FoursquareDetailsResult

        if (code != other.code) return false
        if (foursquareDetailsResponseData != other.foursquareDetailsResponseData) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code.hashCode()
        result = 31 * result + (foursquareDetailsResponseData?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "FoursquareDetailsResult(code=$code, foursquareDetailsResponseData=$foursquareDetailsResponseData)"
    }
}
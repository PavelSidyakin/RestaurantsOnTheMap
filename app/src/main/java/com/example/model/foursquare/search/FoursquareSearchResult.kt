package com.example.model.foursquare.search

import com.example.model.foursquare.search.response.FoursquareSearchResponseData

data class FoursquareSearchResult (
    val resultCode: FoursquareSearchResultCode,
    val foursquareSearchResponseData: FoursquareSearchResponseData?
) {

    override fun toString(): String {
        return "FoursquareSearchResult(resultCode=$resultCode, foursquareSearchResponseData=$foursquareSearchResponseData)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FoursquareSearchResult

        if (resultCode != other.resultCode) return false
        if (foursquareSearchResponseData != other.foursquareSearchResponseData) return false

        return true
    }

    override fun hashCode(): Int {
        var result = resultCode.hashCode()
        result = 31 * result + (foursquareSearchResponseData?.hashCode() ?: 0)
        return result
    }
}
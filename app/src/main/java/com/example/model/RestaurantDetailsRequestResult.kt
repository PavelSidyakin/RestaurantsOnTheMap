package com.example.model

data class RestaurantDetailsRequestResult (
    val code: RestaurantDetailsRequestResultCode,
    val details: RestaurantDetails?
) {

    override fun toString(): String {
        return "RestaurantDetailsRequestResult(code=$code, details='$details')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantDetailsRequestResult

        if (code != other.code) return false
        if (details != other.details) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code.hashCode()
        result = 31 * result + (details?.hashCode() ?: 0)
        return result
    }
}
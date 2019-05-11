package com.example.model

data class RestaurantDetails (
    val name: String? = null,
    val description: String? = null,
    val url: String? = null,
    val rating: Double? = null

) {


    override fun toString(): String {
        return "RestaurantDetails(name=$name, description=$description, url=$url, rating=$rating)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantDetails

        if (name != other.name) return false
        if (description != other.description) return false
        if (url != other.url) return false
        if (rating != other.rating) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (rating?.hashCode() ?: 0)
        return result
    }
}
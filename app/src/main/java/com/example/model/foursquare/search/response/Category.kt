package com.example.model.foursquare.search.response

import com.example.model.GsonSerializable

data class Category (
    val id: String? = null,
    val name: String? = null,
    val pluralName: String? = null,
    val shortName: String? = null,
    val icon: Icon? = null,
    val primary: Boolean? = null

) : GsonSerializable {

    override fun toString(): String {
        return "Category(id=$id, name=$name, pluralName=$pluralName, shortName=$shortName, icon=$icon, primary=$primary)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (id != other.id) return false
        if (name != other.name) return false
        if (pluralName != other.pluralName) return false
        if (shortName != other.shortName) return false
        if (icon != other.icon) return false
        if (primary != other.primary) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (pluralName?.hashCode() ?: 0)
        result = 31 * result + (shortName?.hashCode() ?: 0)
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + (primary?.hashCode() ?: 0)
        return result
    }
}
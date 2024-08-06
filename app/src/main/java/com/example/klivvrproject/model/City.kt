package com.example.klivvrproject.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class City(
    @SerialName("country") val country: String,
    @SerialName("name") val name: String,
    @SerialName("_id") val id: Long,
    @SerialName("coord") val coordinates: Coord
)

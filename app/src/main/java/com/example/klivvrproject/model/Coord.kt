package com.example.klivvrproject.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    @SerialName("lon") val lon: Double,
    @SerialName("lat") val lat: Double
)

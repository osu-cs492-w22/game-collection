package com.example.gamecollection.data

import com.squareup.moshi.Json

data class GameTrailer(
    val count: Int,
    val results: List<Results>
)

data class Results(
    val id: Int,
    val name: String,
    val preview: String,
    val data: MovieURL
)

data class MovieURL(
    @Json(name = "480") val normal: String,
    val max: String
)
package com.example.journey.data.remote.model.cafe

data class CafeItem(
    val title: String,
    val link: String,
    val category: String,
    val description: String,
    val telephone: String?,
    val address: String,
    val roadAddress: String,
    val mapx: String,
    val mapy: String
)

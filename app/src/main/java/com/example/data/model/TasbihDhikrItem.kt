package com.example.data.model

data class TasbihDhikrItem(
    val id: String,
    val bengaliName: String,
    val arabicText: String = "",
    val meaningBn: String = "",
    val virtueBn: String = "",
    val recommendedCount: Int = 33
)

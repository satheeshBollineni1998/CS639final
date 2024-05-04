package com.example.myapplication2


data class SearchResponse(
    val meals: List<Meal>
)
data class Meal(
    val name: String,
    val category: String,
    val area: String,

)
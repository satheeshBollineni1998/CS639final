package com.example.myapplication2
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class FoodItem {
    var name: String? = null
    var imageUrl: String? = null
    var calories: Int = 0
    var macros: Macros? = null

    constructor() // Add a no-argument constructor

    constructor(name: String, imageUrl: String, calories: Int, macros: Macros) {
        this.name = name
        this.imageUrl = imageUrl
        this.calories = calories
        this.macros = macros
    }
}

class Macros {
    var protein: Double = 0.0
    var carbs: Double = 0.0
    var fat: Double = 0.0

    constructor() // Add a no-argument constructor

    constructor(protein: Double, carbs: Double, fat: Double) {
        this.protein = protein
        this.carbs = carbs
        this.fat = fat
    }
}

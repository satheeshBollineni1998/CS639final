
package com.example.myapplication2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EdamamApi {

    @GET("/search")
    fun searchRecipes(
        @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String
    ): Call<SearchResponse>

    @GET("/api/nutrition-details")
    fun analyzeMeal(
        @Query("ingr") ingredients: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String
    ): Call<MealNutrition>
}


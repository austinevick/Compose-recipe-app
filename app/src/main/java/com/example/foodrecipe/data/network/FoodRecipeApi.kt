package com.example.foodrecipe.data.network

import com.example.foodrecipe.model.FoodRecipeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipeApi {
    @GET("/recipes/complexSearch")
    suspend fun getFoodRecipe(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipeModel>
}
package com.example.foodrecipe.data

import com.example.foodrecipe.model.FoodRecipeModel
import com.example.foodrecipe.data.network.FoodRecipeApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipeApi: FoodRecipeApi
) {
    suspend fun getFoodRecipe(queries: Map<String, String>): Response<FoodRecipeModel> {
        return foodRecipeApi.getFoodRecipe(queries)
    }
}
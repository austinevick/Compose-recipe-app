package com.example.foodrecipe.database

import androidx.room.TypeConverter
import com.example.foodrecipe.model.FoodRecipeModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {
    @TypeConverter
    fun foodRecipeToString(foodRecipeModel: FoodRecipeModel): String {
        return Gson().toJson(foodRecipeModel)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipeModel {
        val listType = object : TypeToken<FoodRecipeModel>() {}.type
        return Gson().fromJson(data, listType)
    }
}
package com.example.foodrecipe.dataSource

import com.example.foodrecipe.database.RecipeEntity
import com.example.foodrecipe.database.RecipesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao) {

    fun readRecipes():Flow<List<RecipeEntity>>{
        return recipesDao.readRecipes()
    }
    suspend fun insertRecipes(recipeEntity: RecipeEntity){
         recipesDao.insertRecipe(recipeEntity)
    }
}
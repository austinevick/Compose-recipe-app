package com.example.foodrecipe.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipe.constants.Api

@Entity(tableName = Api.RECIPE_TABLE)
class RecipeEntity {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0


}
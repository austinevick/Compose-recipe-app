package com.example.foodrecipe.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodrecipe.constants.Api.Companion.API_KEY
import com.example.foodrecipe.database.RecipeEntity
import com.example.foodrecipe.repository.Repository
import com.example.foodrecipe.model.FoodRecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    // ROOM DATABASE
    val readRecipesFromDatabase: LiveData<List<RecipeEntity>> = repository.local.readRecipes().asLiveData()

    // RETROFIT
    private val _recipesResponse = MutableStateFlow<UIState>(UIState.Loading)
    val recipesResponse: StateFlow<UIState> = _recipesResponse.asStateFlow()

    private val _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm.asStateFlow()

    init {
        viewModelScope.launch {

            getRecipesFromApi(applyQueries())
        }
    }

    private fun insertRecipes(recipeEntity: RecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipeEntity)
        }

    fun onSearchChanged(text: String) {
        _searchTerm.value = text
        viewModelScope.launch {
            delay(1000)
            getRecipesFromApi(applyQueries())
        }
    }

    private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries["number"] = "30"
        queries["apiKey"] = API_KEY
        queries["query"] = _searchTerm.value
        return queries
    }

    private suspend fun getRecipesFromApi(queries: Map<String, String>) {
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getFoodRecipe(queries)
                _recipesResponse.value = UIState.Success(response.body()!!)
                val foodRecipe = response.body()
                if (foodRecipe != null) {
                    saveToDatabase(foodRecipe)
                }
            } catch (e: Exception) {
                _recipesResponse.value = UIState.Error(e.message.toString())
            }
        } else {
            _recipesResponse.value = UIState.Error("No Internet Connection.")
        }
    }

    private fun saveToDatabase(foodRecipe: FoodRecipeModel) {
        val recipeEntity = RecipeEntity()
        insertRecipes(recipeEntity)
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            else -> false
        }

    }

}


sealed class UIState {
    data object Loading : UIState()
    data class Success(val post: FoodRecipeModel) : UIState()
    data class Error(
        val message: String = "Something went wrong"
    ) : UIState()
}
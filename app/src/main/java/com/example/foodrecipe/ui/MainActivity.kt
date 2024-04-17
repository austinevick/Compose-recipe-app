package com.example.foodrecipe.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import coil.compose.AsyncImage
import com.example.foodrecipe.theme.FoodRecipeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContent {
            FoodRecipeTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                  Navigator(HomeScreen(viewModel = mainViewModel))
                }
            }
        }
    }
}

data class HomeScreen(val viewModel: MainViewModel) : Screen {
    @Composable
    override fun Content() {

        val state = viewModel.recipesResponse.collectAsState()
        val navigator = LocalNavigator.current

        Scaffold(floatingActionButton = {
            FloatingActionButton(onClick = {
                navigator?.push(SearchActivity(viewModel))
            }) {
                Icon(Icons.Outlined.Search, contentDescription = null)
            }
        }) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                when (state.value) {
                    is UIState.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(50.dp),
                          content = { CircularProgressIndicator() })

                    }

                    is UIState.Error -> {
                        val error = (state.value as UIState.Error)
                        Text(text = error.message)
                    }

                    is UIState.Success -> {
                        val data = (state.value as UIState.Success)
                        LazyColumn {
                            items(data.post.results.size) {
                                OutlinedCard(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            model = data.post.results[it].image,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxHeight()
                                        )
                                        Text(
                                            text = data.post.results[it].title,
                                            modifier = Modifier.padding(horizontal = 8.dp)) } }
                            }
                        }
                    }
                }
            }
        }
    }
}


//@Composable
//fun BottomNavigation(){
//    Scaffold(
//        bottomBar = {
//
//        }
//    ) {
//
//    }
//}


sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    data object Search : BottomNavItem("search", Icons.Default.Search, "Search")
    data object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}
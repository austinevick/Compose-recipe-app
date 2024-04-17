package com.example.foodrecipe.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil.compose.AsyncImage
import com.example.foodrecipe.R

data class SearchActivity(val viewModel: MainViewModel) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val focusRequester = remember { FocusRequester() }
        val textFieldLoaded = remember { mutableStateOf(false) }
        val searchText by viewModel.searchTerm.collectAsState()
        val state = viewModel.recipes.collectAsState()

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(20.dp))
          Row (
              modifier = Modifier.padding(horizontal = 8.dp),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically){

        IconButton(
            modifier = Modifier.padding(end = 6.dp),
            onClick = { navigator?.pop() }) {
            Icon(
                painterResource(id = R.drawable.keyboard_backspace),
                null,
                modifier = Modifier.size(30.dp))
        }

              OutlinedTextField(
                value = searchText,
                onValueChange = viewModel::onSearchChanged,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onGloballyPositioned {
                        if (!textFieldLoaded.value) {
                            focusRequester.requestFocus()
                            textFieldLoaded.value = true
                        }
                    }
            )
        }
            Spacer(modifier = Modifier.height(8.dp))

          when(state.value){
              is UIState.Loading ->{ Box(
                  contentAlignment = Alignment.Center,
                  modifier = Modifier.size(50.dp),
                  content = { CircularProgressIndicator() })
              }
          is UIState.Error ->{
              val error = (state.value as UIState.Error)
              Text(text = error.message)
          }
              is UIState.Success->{

        LazyColumn(){
            val data = (state.value as UIState.Success)

            items(data.post.results){
                OutlinedCard(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = it.image,
                            contentDescription = null,
                            modifier = Modifier.fillMaxHeight()
                        )
                        Text(
                            text = it.title,
                            modifier = Modifier.padding(horizontal = 8.dp)) } }
            }
        }
              }
          }

        }


    }
}
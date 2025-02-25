package com.example.todoapp.ui.viewModel

import androidx.lifecycle.ViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import androidx.compose.runtime.mutableStateListOf
import Todo
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


sealed interface TodoUiState {
    data class Success(val todos: List<Todo>): TodoUiState
    object Error: TodoUiState
    object Loading: TodoUiState
}

class TodoViewModel: ViewModel() {
    var todoUiState: TodoUiState by mutableStateOf<TodoUiState>(TodoUiState.Loading)
        private set
    init {
        getTodosList()
    }
    private fun getTodosList() {
        viewModelScope.launch {
            var todosApi: TodosApi? = null
            try {
                todosApi = TodosApi.getInstance()
                todoUiState = TodoUiState.Success(todosApi.getTodos())
            } catch (e: Exception) {
                todoUiState = TodoUiState.Error
            }
        }
    }
}

const val BASE_URL = "https://jsonplaceholder.typicode.com"

interface TodosApi {
    @GET("todos")
    suspend fun getTodos() : List<Todo>
    companion object {
        var todosService: TodosApi? = null
        fun getInstance(): TodosApi {
            if (todosService === null) {
                todosService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TodosApi::class.java)
            }
            return todosService!!
        }
    }
}
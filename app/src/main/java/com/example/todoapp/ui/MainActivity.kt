package com.example.todoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todoapp.ui.theme.TodoAppTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.ui.viewModel.TodoViewModel
import com.example.todoapp.ui.viewModel.TodoUiState
import Todo
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.CenterAlignedTopAppBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                val todoViewModel: TodoViewModel = viewModel()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TodoTitleBar() }
                ) { innerPadding ->
                    TodoScreen(
                        modifier = Modifier.padding(innerPadding),
                        uiState = todoViewModel.todoUiState
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTitleBar() {
    CenterAlignedTopAppBar(
        title = { Text(text = "Todo App") }
    )
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Text(text="Loading...", modifier=modifier)
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Text(text="Error retrieving data from API.", modifier=modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp(todoViewModel: TodoViewModel = viewModel()) {
    Scaffold(
        topBar = { TodoTitleBar() }
    ) { innerPadding ->
        TodoScreen(Modifier.padding(innerPadding), uiState = todoViewModel.todoUiState)
    }
}

@Composable
fun TodoScreen(modifier: Modifier = Modifier, uiState: TodoUiState) {
    when (uiState) {
        is TodoUiState.Loading -> LoadingScreen(modifier)
        is TodoUiState.Success -> TodoList(modifier, uiState.todos)
        is TodoUiState.Error -> ErrorScreen(modifier)
    }
}

@Composable
fun TodoList(modifier: Modifier = Modifier, todos: List<Todo>) {
    LazyColumn(modifier = modifier) {
        items(todos) { todo ->
            Text(
                text = todo.title,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
            HorizontalDivider(color = Color.LightGray, thickness = 2.dp)
        }
    }
}
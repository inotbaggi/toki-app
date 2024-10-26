package me.baggi.schedule.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import me.baggi.schedule.data.UiState
import me.baggi.schedule.data.FacultyDTO

/*@Composable
fun FacultyListScreen(viewModel: DataViewModel = DataViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.loadFaculties()
    }
    val state by viewModel.state.collectAsState()

    when (state) {
        is UiState.Loading -> {
            LoadingScreen()
        }
        is UiState.Success -> {
            val data = (state as UiState.Success<List<FacultyDTO>>).data
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(
                    text = "Факультеты",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                data.forEach { faculty ->
                    Text(
                        text = faculty.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { }
                    )
                    Divider()
                }
            }
        }
        is UiState.Error -> {
            ErrorScreen((state as UiState.Error).exception.message ?: "Unknown error")
        }
    }
}*/

@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Error: $message", color = Color.Red)
            Button(onClick = {}) {
                Text("Retry")
            }
        }
    }
}



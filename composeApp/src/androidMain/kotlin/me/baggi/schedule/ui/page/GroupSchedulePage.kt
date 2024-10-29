package me.baggi.schedule.ui.page

import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import me.baggi.schedule.data.saveGroupId

@Composable
fun GroupSchedulePage(id: Long) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    Button(
        onClick = {
            coroutine.launch {
                saveGroupId(context, id)
            }
        },
    ) {
        Text("Это моя группа!")
    }
    /*val state by viewModel.dataFlow.collectAsState(0)

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 24.dp
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Факультеты",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success<*> -> {
                val data = (state as UiState.Success<List<FacultyDTO>>).data
                LazyColumn {
                    items(data) {
                        FacultyItem(it, navController)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            is UiState.Error -> {
                val message = (state as UiState.Error).exception.message ?: "Unknown error"
                ErrorComponent(message)
            }
        }
    }*/
}
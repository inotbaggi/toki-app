package me.baggi.schedule.ui.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import me.baggi.schedule.ui.viewmodel.FacultiesViewModel
import me.baggi.schedule.data.FacultyDTO
import me.baggi.schedule.data.UiState
import me.baggi.schedule.ui.component.ErrorComponent
import me.baggi.schedule.ui.component.LoadingComponent
import me.baggi.schedule.ui.innerPaddings

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FacultiesPage(navController: NavHostController, viewModel: FacultiesViewModel = viewModel()) {
    val state by viewModel.dataFlow.collectAsState(0)

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPaddings),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Факультеты",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                when (state) {
                    is UiState.Loading -> {
                        LoadingComponent()
                    }

                    is UiState.Success<*> -> {
                        val data = (state as UiState.Success<List<FacultyDTO>>).data
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(data) { faculty ->
                                FacultyItem(faculty, navController)
                            }
                        }
                    }

                    is UiState.Error -> {
                        val message = (state as UiState.Error).exception.message ?: "Unknown error"
                        ErrorComponent(message, navController)
                    }
                }
            }
        }
    )
}

@Composable
fun FacultyItem(facultyDTO: FacultyDTO, navController: NavHostController) {
    Card(
        onClick = {
            navController.navigate("faculty/${facultyDTO.id}")
        },
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = facultyDTO.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

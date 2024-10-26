package me.baggi.schedule.ui.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import me.baggi.schedule.data.FacultiesViewModel
import me.baggi.schedule.data.FacultyDTO
import me.baggi.schedule.data.UiState
import me.baggi.schedule.data.getGroupId
import me.baggi.schedule.ui.component.ErrorComponent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FacultiesPage(navController: NavHostController, viewModel: FacultiesViewModel = viewModel()) {
    val state by viewModel.dataFlow.collectAsState(0)

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
                        Spacer(modifier = Modifier.height(4.dp))
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

@Composable
fun FacultyItem(facultyDTO: FacultyDTO, navController: NavHostController) {
    Card(
        onClick = {
            navController.navigate("faculty/${facultyDTO.id}")
        }
    ) {
        Text(
            text = facultyDTO.name,
            fontSize = 22.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}
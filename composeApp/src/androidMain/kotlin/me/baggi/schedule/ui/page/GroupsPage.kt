package me.baggi.schedule.ui.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import me.baggi.schedule.data.*
import me.baggi.schedule.data.viewmodel.GroupsViewModel
import me.baggi.schedule.ui.component.ErrorComponent
import me.baggi.schedule.ui.component.LoadingComponent
import me.baggi.schedule.ui.innerPaddings

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupsPage(facultyId: Long, navController: NavHostController, viewModel: GroupsViewModel = viewModel()) {
    val state by viewModel.dataFlow.collectAsState()

    LaunchedEffect(facultyId) {
        viewModel.loadData(facultyId)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(innerPaddings)
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
                text = "Группы",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is UiState.Loading -> {
                LoadingComponent()
            }

            is UiState.Success<*> -> {
                val data = (state as UiState.Success<List<GroupDTO>>).data
                LazyColumn {
                    items(data) {
                        GroupItem(it, navController)
                        Spacer(modifier = Modifier.height(8.dp))
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
fun GroupItem(groupDTO: GroupDTO, navController: NavHostController) {
    Card(
        onClick = {
            navController.navigate("group/${groupDTO.id}")
        }
    ) {
        Text(
            text = groupDTO.name,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        )
    }
}

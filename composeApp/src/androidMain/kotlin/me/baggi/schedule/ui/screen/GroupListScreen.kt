package me.baggi.schedule.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.baggi.schedule.data.GroupDTO
import me.baggi.schedule.data.Repository

@Composable
fun GroupListScreen(facultyId: Long) {
    val coroutineScope = rememberCoroutineScope()
    var groups by remember { mutableStateOf(emptyList<GroupDTO>()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            groups = Repository.getGroups(facultyId)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        groups.forEach { group ->
            Text(
                text = group.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { /* Navigate to schedule */ }
            )
            Divider()
        }
    }
}

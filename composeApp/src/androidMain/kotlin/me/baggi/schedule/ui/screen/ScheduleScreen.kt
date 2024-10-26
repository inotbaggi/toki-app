package me.baggi.schedule.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.baggi.schedule.data.Repository
import me.baggi.schedule.data.ScheduleDayDTO

@Composable
fun ScheduleScreen(groupId: Long) {
    val coroutineScope = rememberCoroutineScope()
    var schedule by remember { mutableStateOf(emptyList<ScheduleDayDTO>()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            schedule = Repository.getScheduleForGroup(groupId)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        schedule.forEach { day ->
            Text(text = "Day: ${day.time}", modifier = Modifier.padding(8.dp))
            day.lessons.forEach { lesson ->
                Text(text = "${lesson.subject} - ${lesson.teacher}", modifier = Modifier.padding(8.dp))
            }
            Divider()
        }
    }
}

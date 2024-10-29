package me.baggi.schedule.ui.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import me.baggi.schedule.data.*
import me.baggi.schedule.ui.component.ErrorComponent
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(navController: NavHostController, viewModel: TodaySchedulesViewModel = viewModel()) {
    val currentDate = LocalDate.now()

    val dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val dayOfMonth = currentDate.dayOfMonth
    val month = currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

    val userGroupId = getGroupId(LocalContext.current).collectAsState(null)

    val state by viewModel.dataFlow.collectAsState()

    val selectedDay = rememberSaveable { mutableStateOf(currentDate.dayOfMonth) }

    LaunchedEffect(userGroupId) {
        if (userGroupId.value != null) {
            viewModel.loadData(userGroupId.value!!)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 36.dp
            )
    ) {
        Text(
            text = "Сегодня",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(128,128,128)
        )
        Text(
            text = "$dayOfWeek, $dayOfMonth $month",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow {
            items(7) {
                val date = currentDate.plusDays(it.toLong())
                HomeDayItem(date, date.dayOfMonth == selectedDay.value) {
                    selectedDay.value = date.dayOfMonth
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (userGroupId.value == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Text(
                    text = "\uD83D\uDE30",
                    textAlign = TextAlign.Center,
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ой... Похоже ты не выбрал(а) свою группу",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(onClick = {
                    navController.navigate(Page.FACULTY_LIST)
                }) {
                    Text("Перейти")
                }
            }
        } else {
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
                    val data = (state as UiState.Success<ScheduleDayDTO?>).data
                    if (data != null) {
                        LazyColumn {
                            items(data.lessons) {
                                ScheduleCardItem(lesson = it)
                            }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "\uD83D\uDE22",
                                textAlign = TextAlign.Center,
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Расписания нет!",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Возможно на этот день нет расписания или стоит подождать...",
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
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
}

@Composable
fun ScheduleCardItem(lesson: LessonDTO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = lesson.lessonName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                lesson.teachers.forEachIndexed { index, teacher ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = teacher,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f) // для распределения пространства
                        )

                        Text(
                            text = "Кабинет: ${lesson.cabinets.getOrNull(index) ?: "N/A"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeDayItem(localDate: LocalDate, selected: Boolean, onClick: () -> Unit) {
    val dayOfWeek = localDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val dayOfMonth = localDate.dayOfMonth
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.background)
            .border(
                width = 4.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            ).clickable {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp
                )
        ) {
            Text(
                text = dayOfWeek,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color(128,128,128)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$dayOfMonth",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp
            )
        }
    }

}
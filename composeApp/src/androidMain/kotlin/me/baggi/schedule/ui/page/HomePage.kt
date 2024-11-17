package me.baggi.schedule.ui.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.baggi.schedule.data.*
import me.baggi.schedule.ui.viewmodel.TodaySchedulesViewModel
import me.baggi.schedule.ui.component.*
import me.baggi.schedule.ui.innerPaddings
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomePage(
    notificationPermissionState: PermissionState,
    showNotifyCard: MutableState<Boolean>,
    showUpdateCard: MutableState<Boolean>,
    navController: NavHostController,
    viewModel: TodaySchedulesViewModel = viewModel()
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val currentDate = LocalDate.now()

    val dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val dayOfMonth = currentDate.dayOfMonth
    val month = currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

    val state by viewModel.dataFlow.collectAsState()

    val configManager = remember { ConfigManager(context) }
    val groupId by configManager.groupId.collectAsState(initial = null)
    val isTeacherFlow = configManager.isTeacher
    var isTeacher by remember { mutableStateOf("false") }
    var expandedMenu by remember { mutableStateOf(false) }
    val selectedDay = remember { mutableStateOf(currentDate.dayOfMonth) }

    LaunchedEffect(Unit) {
        isTeacher = isTeacherFlow.first() ?: "false"
    }

    LaunchedEffect(groupId) {
        val userGroupId = groupId ?: return@LaunchedEffect
        viewModel.loadData(userGroupId)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddings)
            .zIndex(999f)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 4.dp, end = 4.dp, bottom = 16.dp)
        ) {
            if (showNotifyCard.value) {
                NotifyCard(
                    "\uD83E\uDD7A Разреши уведомления",
                    { showNotifyCard.value = false },
                    listOf(
                        NotifyButton(
                            Icons.Default.Check,
                            MaterialTheme.colorScheme.primary,
                            { notificationPermissionState.launchPermissionRequest() },
                        )
                    )
                )
                Spacer(Modifier.height(8.dp))
            }
            if (showUpdateCard.value) {
                NotifyUpdateCard({ showUpdateCard.value = false }, navController)
            }
        }

    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(innerPaddings)
            .padding(start = 16.dp, end = 16.dp, top = 36.dp)
    ) {
        Box(modifier = Modifier.align(Alignment.TopEnd).zIndex(2f)) {
            IconButton(onClick = { expandedMenu = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Показать меню")
            }
            DropdownMenu(
                expanded = expandedMenu,
                onDismissRequest = { expandedMenu = false }
            ) {
                DropdownMenuItem(text = {
                    Text("ℹ\uFE0F Обратная связь", fontSize = 18.sp)
                }, onClick = {
                    uriHandler.openUri("https://t.me/inotbaggi")
                    expandedMenu = false
                })
                DropdownMenuItem(text = {
                    Text("📖 Я ${if (isTeacher == "true") "не " else ""}преподаватель")
                }, onClick = {
                    runBlocking {
                        val newValue = if (isTeacher == "true") "false" else "true"
                        configManager.setIsTeacher(newValue)
                        isTeacher = newValue
                        expandedMenu = false
                    }
                })
                DropdownMenuItem(text = {
                    Text("\uD83D\uDEE0\uFE0F Dev tools", fontSize = 18.sp)
                }, onClick = {
                    navController.navigate("devtools")
                    expandedMenu = false
                })
            }
        }

        Column(modifier = Modifier.zIndex(1f)) {
            Row {
                Column {
                    Text(
                        text = "Сегодня",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$dayOfWeek, $dayOfMonth $month",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
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
            if (groupId == null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().fillMaxHeight()
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
                        navController.navigate(PageType.FACULTY_LIST.name)
                    }) {
                        Text("Перейти")
                    }
                }
            } else {
                when (state) {
                    is UiState.Loading -> {
                        LoadingComponent()
                    }

                    is UiState.Success<*> -> {
                        val data = (state as UiState.Success<ScheduleDayDTO?>).data
                        if (data != null) {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                itemsIndexed(data.lessons) { index, lesson ->
                                    val time = DataStorage.lessonPeriods[data.intervalId]
                                    ScheduleCardItem(time?.times?.get(index), lesson = lesson)
                                }
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth().fillMaxHeight()
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
                                    text = "Возможно стоит подождать...",
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
}


@Composable
fun ScheduleCardItem(lessonTime: String?, lesson: LessonDTO) {
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = lesson.lessonName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = lessonTime ?: "??:??",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

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
                            modifier = Modifier.weight(1f)
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
                color = Color(128, 128, 128)
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
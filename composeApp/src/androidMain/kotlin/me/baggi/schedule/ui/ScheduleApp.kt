package me.baggi.schedule.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import me.baggi.schedule.MyIcons
import me.baggi.schedule.data.CacheRepository
import me.baggi.schedule.data.Repository
import me.baggi.schedule.ui.component.TabBarItem
import me.baggi.schedule.ui.component.TabView
import me.baggi.schedule.ui.page.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleApp() {
    val navController = rememberNavController()
    var isLoadedMain by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        CacheRepository.lessonPeriods = Repository.getLessonTimes()
        isLoadedMain = true
    }

    if (isLoadedMain) {
        val tabs = listOf(
            TabBarItem(
                page = Page.HOME,
                title = "Главная",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                compose = { HomePage(navController) }
            ),
            TabBarItem(
                page = Page.FACULTY_LIST,
                title = "Факультеты",
                selectedIcon = MyIcons.book,
                unselectedIcon = MyIcons.book,
                compose = { FacultiesPage(navController) }
            ),
            TabBarItem(
                page = Page.OTHER,
                title = "Прочее",
                selectedIcon = Icons.Filled.Menu,
                unselectedIcon = Icons.Outlined.Menu,
                compose = { OtherPage(navController) }
            )
        )
        Scaffold(
            bottomBar = { TabView(tabs, navController) },
        ) {
            NavHost(navController = navController, startDestination = tabs[0].title) {
                tabs.map { tab ->
                    composable(tab.title) {
                        tab.compose()
                    }
                }

                composable("faculty/{id}", arguments = listOf(navArgument("id") {
                    type = NavType.LongType
                })) {
                    val facultyId = it.arguments?.getLong("id") ?: return@composable
                    GroupsPage(facultyId, navController)
                }

                composable("group/{id}", arguments = listOf(navArgument("id") {
                    type = NavType.LongType
                })) {
                    val groupId = it.arguments?.getLong("id") ?: return@composable
                    GroupSchedulePage(groupId)
                }

                composable("devtools") {
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text("LessonPeriods: ${CacheRepository.lessonPeriods}")
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
                Text(
                    "Актуализируем данные...",
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

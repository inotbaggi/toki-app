package me.baggi.schedule.ui

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay
import me.baggi.schedule.MyIcons
import me.baggi.schedule.data.CacheRepository
import me.baggi.schedule.web.Repository
import me.baggi.schedule.ui.component.ErrorComponent
import me.baggi.schedule.ui.component.LoadingComponent
import me.baggi.schedule.ui.component.TabBarItem
import me.baggi.schedule.ui.component.TabView
import me.baggi.schedule.ui.page.*

lateinit var innerPaddings: PaddingValues

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScheduleApp() {
    val navController = rememberNavController()
    var error by remember { mutableStateOf<String?>(null) }
    var isLoadedMain by remember { mutableStateOf(false) }

    val showNotifyCard = rememberSaveable { mutableStateOf(true) }
    val showUpdateCard = rememberSaveable { mutableStateOf(false) }
    val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS) { isGranted ->
        if (isGranted) {
            showNotifyCard.value = false
        }
    }
    if (notificationPermissionState.status.isGranted) {
        showNotifyCard.value = false
    }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            CacheRepository.lessonPeriods = Repository.getLessonTimes()?.associateBy({it.id}, {it})
                ?: throw Exception("Null data")
            CacheRepository.appInfo = Repository.getAppInfo() ?: throw Exception("Null data")

            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (CacheRepository.appInfo.lastVersion != pInfo.versionName) {
                showUpdateCard.value = true
            }

            delay(500)
            isLoadedMain = true
            if (!notificationPermissionState.status.shouldShowRationale) {
                notificationPermissionState.launchPermissionRequest()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            error = e.message
        }
    }
    when {
        error != null -> {
            ErrorComponent(error ?: "Неизвестная ошибка...")
        }
        isLoadedMain -> {
            val tabs = listOf(
                TabBarItem(
                    page = Page.HOME,
                    title = "Главная",
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    compose = { HomePage(notificationPermissionState, showNotifyCard, showUpdateCard, navController) }
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
            ) { innerPadding ->
                innerPaddings = innerPadding
                NavHost(navController = navController, startDestination = tabs[0].page.name) {
                    tabs.map { tab ->
                        composable(tab.page.name) {
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
                        DevPage()
                    }
                }
            }
        }
        else -> {
            LoadingComponent()
        }
    }
}

package me.baggi.schedule.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import me.baggi.schedule.MyIcons
import me.baggi.schedule.ui.component.TabBarItem
import me.baggi.schedule.ui.component.TabView
import me.baggi.schedule.ui.page.FacultiesPage
import me.baggi.schedule.ui.page.GroupsPage
import me.baggi.schedule.ui.page.HomePage
import me.baggi.schedule.ui.page.Page

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleApp() {
    val navController = rememberNavController()
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
            unselectedIcon = Icons.Outlined.Menu
        )
    )
    MaterialTheme {
        Scaffold(
            bottomBar = { TabView(tabs, navController) }
        ) {
            NavHost(navController = navController, startDestination = tabs[0].title) {
                tabs.map { tab ->
                    composable(tab.title) {
                        tab.compose()
                    }
                }

                composable("faculty/{id}", arguments = listOf(navArgument("id"){
                    type = NavType.LongType
                })) {
                    val facultyId = it.arguments?.getLong("id") ?: return@composable
                    GroupsPage(facultyId, navController)
                }
            }
        }
    }
}

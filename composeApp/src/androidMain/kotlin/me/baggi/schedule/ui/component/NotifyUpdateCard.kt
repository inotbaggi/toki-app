package me.baggi.schedule.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import me.baggi.schedule.data.DataStore

@Composable
fun NotifyUpdateCard(onHide: () -> Unit, navController: NavHostController) {
    NotifyCard(
        "ℹ️ Доступно обновление! (v${DataStore.appInfo.lastVersion})",
        onHide,
        listOf(
            NotifyButton(
                Icons.Filled.Download,
                MaterialTheme.colorScheme.primary,
                { navController.navigate("update") }
        ))
    )
}

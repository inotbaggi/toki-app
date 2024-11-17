package me.baggi.schedule.ui.page

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import me.baggi.schedule.data.ConfigManager

@Composable
fun GroupSchedulePage(id: Long) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val configManager = remember { ConfigManager(context) }
    Button(
        onClick = {
            coroutine.launch {
                configManager.setGroupId(id)
            }
        },
    ) {
        Text("Это моя группа!")
    }
}
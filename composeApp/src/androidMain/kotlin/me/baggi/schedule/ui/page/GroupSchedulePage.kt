package me.baggi.schedule.ui.page

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import me.baggi.schedule.data.saveGroupId

@Composable
fun GroupSchedulePage(id: Long) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    Button(
        onClick = {
            coroutine.launch {
                saveGroupId(context, id)
            }
        },
    ) {
        Text("Это моя группа!")
    }
}
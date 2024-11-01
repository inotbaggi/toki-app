package me.baggi.schedule.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.baggi.schedule.data.CacheRepository

@Composable
fun NotifyUpdateCard(onHide: () -> Unit) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Доступна новая версия v${CacheRepository.appInfo.lastVersion}",
                fontWeight = FontWeight.Bold
            )
            Row {
                Button(onClick = {
                    showDialog = true
                }) {
                    Text("Установить")
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = {
                    onHide()
                }) {
                    Text("Скрыть :(")
                }
            }
        }
        if (showDialog) {
            UpdateDialog(
                context = context, // URL APK-файла
                onDismissRequest = { showDialog = false } // Закрытие диалога после загрузки
            )
        }
    }
}
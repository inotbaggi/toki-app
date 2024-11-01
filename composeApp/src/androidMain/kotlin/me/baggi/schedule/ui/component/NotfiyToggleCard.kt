package me.baggi.schedule.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotifyToggleCard(notificationPermissionState: PermissionState, onHide: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            modifier = Modifier.padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 8.dp),
            text = "Хочешь узнавать расписание первым? Включи уведомления об изменении расписания!",
            fontWeight = FontWeight.Bold
        )
        Row(modifier = Modifier.padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 12.dp)) {
            Button(onClick = {
                notificationPermissionState.launchPermissionRequest()
            }) {
                Text("Разрешить")
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = {
                onHide()
            }) {
                Text("Скрыть :(")
            }
        }
    }
}
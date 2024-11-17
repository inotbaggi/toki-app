package me.baggi.schedule.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.UUID

@Composable
fun NotifyCard(
    text: String,
    onHide: () -> Unit,
    buttons: List<NotifyButton> = emptyList()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    text = text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    buttons.forEach { button ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = button.backgroundColor),
                            shape = RoundedCornerShape(4.dp),
                            onClick = button.onClick,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                button.icon,
                                contentDescription = UUID.randomUUID().toString(),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp)
                            )
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(4.dp),
                        onClick = onHide,
                        modifier = Modifier.size(28.dp),
                    ) {
                        Icon(
                            Icons.Filled.Close, contentDescription = "close",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}
data class NotifyButton(
    val icon: ImageVector,
    val backgroundColor: Color,
    val onClick: () -> Unit
)
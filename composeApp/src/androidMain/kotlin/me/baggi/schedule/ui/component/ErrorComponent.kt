package me.baggi.schedule.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ErrorComponent(message: String, navController: NavController? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(
            text = "❌",
            textAlign = TextAlign.Center,
            fontSize = 48.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Произошла непредвиденная ошибка!",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Проверь соединение с интернетом. Если ошибка не пропадает попробуй обратиться в поддержку",
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Button(onClick = {
            if (navController != null) {
                val id = navController.currentDestination?.id
                navController.popBackStack(id!!,true)
                navController.navigate(id)
            }
        }) {
            Text("Попробовать снова", color = Color.White)
        }
        Text(text = "Ошибка (сообщи в тех поддержку при обращении): $message", color = Color.Gray)
    }
}
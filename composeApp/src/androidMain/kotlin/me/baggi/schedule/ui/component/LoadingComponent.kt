package me.baggi.schedule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

val loadingTexts = listOf(
    "Считаем прогулы...",
    "Проверяем студенческие...",
    "Сдаем деньги на шторы...",
    "Пишем конспекты..."
)

@Composable
fun LoadingComponent() {
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
                loadingTexts.random(),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
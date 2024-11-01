package me.baggi.schedule.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.baggi.schedule.data.CacheRepository
import me.baggi.schedule.ui.innerPaddings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevPage() {
    val context = LocalContext.current
    val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPaddings),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Уголок разработчика",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Зачем ты тут? Кофе закончился вчера, поэтому не предлагаю")
                Text("Build version: ${pInfo.versionName}")
                Text("Package name: ${pInfo.packageName}")
                Column {
                    Text("Caching info:")
                    Text("- LessonPeriods: ${CacheRepository.lessonPeriods}")
                }
                CacheRepository.metricParams.map {
                    Text("${it.key}: ${it.value}")
                }
            }
        }
    )
}
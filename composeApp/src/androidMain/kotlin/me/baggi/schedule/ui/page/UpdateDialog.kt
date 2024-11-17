package me.baggi.schedule.ui.page

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.baggi.schedule.data.FacultyDTO
import me.baggi.schedule.data.UiState
import me.baggi.schedule.ui.component.ErrorComponent
import me.baggi.schedule.ui.component.LoadingComponent
import me.baggi.schedule.ui.innerPaddings
import me.baggi.schedule.web.ApiClient
import me.baggi.schedule.web.Repository
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.io.use

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePage(
    context: Context
) {
    val isError by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    var fileSizeText by remember { mutableStateOf("") }

    coroutineScope.launch {
        val file = File(context.cacheDir, "update.apk")
        downloadFile("${ApiClient.BASE_URL}app/download", file, onProgress = { downloaded, total ->
            progress = downloaded.toFloat() / total
            fileSizeText = formatFileSize(downloaded, total)
        })
        installApk(context, file)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Обновление клиента",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        content = { paddingValues ->
            if (isError) {
                ErrorComponent("Ошибка при загрузке")
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = fileSizeText,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    )
}

fun installApk(context: Context, apkFile: File) {
    val uri = FileProvider.getUriForFile(context, "me.baggi.schedule.fileprovider", apkFile)

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/vnd.android.package-archive")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}

fun formatFileSize(downloaded: Long, total: Long): String {
    val downloadedMB = downloaded.toDouble() / (1024 * 1024)
    val totalMB = total.toDouble() / (1024 * 1024)
    return String.format("%.2f МБ скачано", downloadedMB)
}

suspend fun downloadFile(
    url: String,
    outputFile: File,
    onProgress: (downloaded: Long, total: Long) -> Unit
) {
    withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connect()

        val totalSize = connection.getHeaderField("Content-Length")?.toLong() ?: 0
        var downloadedSize: Long = 0
        val input: InputStream = connection.inputStream
        val output = outputFile.outputStream()

        try {
            val buffer = ByteArray(32024)
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
                downloadedSize += bytesRead
                onProgress(downloadedSize, totalSize)
            }
        } finally {
            input.close()
            output.close()
        }
    }
}
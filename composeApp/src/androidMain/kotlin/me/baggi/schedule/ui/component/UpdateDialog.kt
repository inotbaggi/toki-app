package me.baggi.schedule.ui.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.baggi.schedule.data.ApiClient
import me.baggi.schedule.web.Repository
import java.io.File
import kotlin.io.use

@Composable
fun UpdateDialog(
    context: Context,
    onDismissRequest: () -> Unit
) {
    var progress by remember { mutableStateOf(0f) }
    var downloadedSize by remember { mutableStateOf(0L) }
    var totalSize by remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    // Запускаем загрузку при создании компонента
    DisposableEffect(Unit) {
        val apkFile = File(context.cacheDir, "update.apk")

        coroutineScope.launch {
            downloadApkWithProgress(apkFile) { downloaded, total ->
                downloadedSize = downloaded
                totalSize = total
                progress = if (total > 0) downloaded.toFloat() / total else 0f
            }
            installApk(context, apkFile)
            onDismissRequest() // Закрытие диалога после установки
        }

        onDispose { }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        title = { Text(text = "Скачивание обновления") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "${downloadedSize / (1024 * 1024)} МБ / ${totalSize / (1024 * 1024)} МБ")
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

suspend fun downloadApkWithProgress(
    outputFile: File,
    onProgress: (downloaded: Long, total: Long) -> Unit
) {
    val response = Repository.getLastApplication()
    val contentLength = response.contentLength() ?: -1L

    outputFile.outputStream().use { outputStream ->
        val channel: ByteReadChannel = response.bodyAsChannel()
        var totalBytesRead = 0L
        val buffer = ByteArray(8192)

        while (!channel.isClosedForRead) {
            val bytesRead = channel.readAvailable(buffer)
            if (bytesRead == -1) break

            outputStream.write(buffer, 0, bytesRead)
            totalBytesRead += bytesRead
            onProgress(totalBytesRead, contentLength)  // обновляем через callback
        }
    }
}
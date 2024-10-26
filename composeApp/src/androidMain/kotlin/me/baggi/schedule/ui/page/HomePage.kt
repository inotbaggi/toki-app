package me.baggi.schedule.ui.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import me.baggi.schedule.data.getGroupId
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(navController: NavHostController) {
    val currentDate = LocalDate.now()

    val dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val dayOfMonth = currentDate.dayOfMonth
    val month = currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

    val userGroupId = getGroupId(LocalContext.current).collectAsState(0)

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 36.dp
            )
    ) {
        Text(
            text = "Сегодня",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(128,128,128)
        )
        Text(
            text = "$dayOfWeek, $dayOfMonth $month",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow {
            items(7) {
                HomeDayItem(currentDate.plusDays(it.toLong()))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (userGroupId.value == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Text(
                    text = "\uD83D\uDE30",
                    textAlign = TextAlign.Center,
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ой... Похоже ты не выбрал(а) свою группу",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(onClick = {
                    navController.navigate(Page.FACULTY_LIST)
                }) {
                    Text("Перейти")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeDayItem(localDate: LocalDate) {
    val dayOfWeek = localDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val dayOfMonth = localDate.dayOfMonth
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .padding(end = 16.dp)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomEnd = 16.dp,
                    bottomStart = 16.dp
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = dayOfWeek,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = if ((6..7).contains(localDate.dayOfWeek.value)) Color.Red else Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$dayOfMonth",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
        }
    }

}
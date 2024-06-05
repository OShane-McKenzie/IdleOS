
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.SimpleAnimator
import kotlinx.coroutines.delay
import kotlinx.datetime.*
import objects.AnimationStyle
import objects.Sizes


@Composable
fun IdleCalendar(modifier: Modifier = Modifier) {
    val currentMoment = Clock.System.now()
    val currentDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
    val currentDate = currentDateTime.date
    val month = currentDate.month
    val year = currentDate.year

    var animate by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit){
        delay(10)
        animate = true
    }
    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp)
    ) {
        if(animate){
            SimpleAnimator(
                style = AnimationStyle.SCALE_IN_CENTER
            ) {
                Column(modifier = Modifier.wrapContentSize()) {
                    MonthHeader(month = month, year = year)
                    Spacer(modifier = Modifier.height(Sizes.eight.dp))
                    DaysOfWeekHeader()
                    Spacer(modifier = Modifier.height(Sizes.three.dp))
                    DaysGrid(currentDate = currentDate)
                }
            }
        }
    }
}

@Composable
fun MonthHeader(month: Month, year: Int) {

    Text(
        text = "${month.name.lowercase().replaceFirstChar { it.uppercase() }} $year",
        fontSize = 24.sp,
        color = contentProvider.globalTextColor.value,
        modifier = Modifier.background(
            color = contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.value),
            shape = RoundedCornerShape(percent = Sizes.thirteen)
        ).padding(8.dp)
    )
}

@Composable
fun DaysOfWeekHeader() {
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Row(
        modifier = Modifier.background(
            color = contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.value),
            shape = RoundedCornerShape(percent = Sizes.thirteen)
        )
    ) {
        daysOfWeek.forEach{
            Text(
                text = it,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                color = contentProvider.globalTextColor.value
            )
        }
    }
}

@Composable
fun DaysGrid(currentDate: LocalDate) {
    val firstDayOfMonth = LocalDate(currentDate.year, currentDate.month, 1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.isoDayNumber % 7 // Sunday = 0
    val daysInMonth = currentDate.month.length(isLeapYear(currentDate.year))

    val totalCells = (firstDayOfWeek + daysInMonth + 6) / 7 * 7 // Total cells to accommodate all days in month including empty spaces
    val daysList = MutableList(totalCells) { "" }
    var today = getDateTimeToString().split(" ")[0].split("-")[2]
    if(today.startsWith("0")){
        today = today.replace("0","")
    }
    for (day in 1..daysInMonth) {
        daysList[firstDayOfWeek + day - 1] = day.toString()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(Sizes.eight.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.value),
                shape = RoundedCornerShape(percent = Sizes.three)
            ),
        verticalArrangement = Arrangement.Center
    ) {
        items(daysList) { day ->
            if(
                today == day
            ){
                DayBox(day = day, true)
            }else{
                DayBox(day = day, false)
            }

        }
    }
}

@Composable
fun DayBox(day: String, isToday:Boolean = false) {
    Box(
        modifier = Modifier
            .padding(3.dp)
            .clickable {  }
            .height(89.dp)
            .width(89.dp)
            .background(contentProvider.globalColor.value, shape = RoundedCornerShape(8)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            color = contentProvider.globalTextColor.value,
            fontWeight = if(isToday) FontWeight.ExtraBold else FontWeight.Normal,
            fontSize = if(isToday) 20.sp else 16.sp
        )
    }
}

fun Month.length(isLeapYear: Boolean): Int {
    return when (this) {
        Month.JANUARY -> 31
        Month.FEBRUARY -> if (isLeapYear) 29 else 28
        Month.MARCH -> 31
        Month.APRIL -> 30
        Month.MAY -> 31
        Month.JUNE -> 30
        Month.JULY -> 31
        Month.AUGUST -> 31
        Month.SEPTEMBER -> 30
        Month.OCTOBER -> 31
        Month.NOVEMBER -> 30
        Month.DECEMBER -> 31
    }
}

fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

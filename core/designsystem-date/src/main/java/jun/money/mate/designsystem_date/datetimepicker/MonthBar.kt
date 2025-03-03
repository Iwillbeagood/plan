package jun.money.mate.designsystem_date.datetimepicker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.nonScaledSp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MonthBar(
    month: LocalDate,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val enterTransition = expandIn(expandFrom = Alignment.Center, clip = false) + fadeIn()
    val exitTransition = shrinkOut(shrinkTowards = Alignment.Center, clip = false) + fadeOut()

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterStart),
            visible = canMoveToYearBefore(month),
            enter = enterTransition,
            exit = exitTransition
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                onClick = onPrev,
                enabled = canMoveToYearBefore(month),
                color = MaterialTheme.colorScheme.surfaceDim,
                shadowElevation = 4.dp,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp),
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    contentDescription = null
                )
            }
        }
        Text(
            text = formatDateBasedOnYear(month),
            style = TypoTheme.typography.titleLargeB.nonScaledSp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterEnd),
            visible = canMoveToNextMonth(month),
            enter = enterTransition,
            exit = exitTransition
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                onClick = onNext,
                enabled = canMoveToNextMonth(month),
                color = MaterialTheme.colorScheme.surfaceDim,
                shadowElevation = 4.dp,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp),
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    }
}

private fun canMoveToNextMonth(month: LocalDate): Boolean {
    val currentMonth = LocalDate.now().withDayOfMonth(1)
    return month.isBefore(currentMonth)
}

private fun canMoveToYearBefore(month: LocalDate): Boolean {
    val oneYearAgo = month.minusYears(1)
    return !month.isBefore(oneYearAgo)
}

private fun formatDateBasedOnYear(date: LocalDate): String {
    val currentYear = LocalDate.now().year

    return if (date.year == currentYear) {
        date.format(DateTimeFormatter.ofPattern("M월")) // 올해면 "2월"
    } else {
        date.format(DateTimeFormatter.ofPattern("yyyy년 M월")) // 작년 또는 그 외 연도면 "2024년 3월"
    }
}

@Preview
@Composable
private fun MonthBarPreview() {
    JunTheme {
        MonthBar(
            month = LocalDate.now().minusMonths(1),
            onPrev = { },
            onNext = { }
        )
    }
}
package jun.money.mate.save.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.BottomToTopSlideFadeAnimatedVisibility
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.UnderlineTextField
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem_date.datetimepicker.PeriodPicker
import jun.money.mate.designsystem_date.datetimepicker.YearMonthPicker
import jun.money.mate.model.save.SavingsType
import jun.money.mate.model.save.SavingsType.Companion.title
import java.time.YearMonth

@Composable
internal fun SaveCategories(
    onCategorySelected: (SavingsType?) -> Unit,
    modifier: Modifier = Modifier,
    selectedCategory: SavingsType? = null,
) {
    Crossfade(
        targetState = selectedCategory == null,
        modifier = modifier.padding(10.dp)
    ) {
        when (it) {
            true -> {
                CategoryField(
                    onCategorySelected = onCategorySelected,
                )
            }

            false -> {
                if (selectedCategory != null) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        shadowElevation = 4.dp,
                        border = BorderStroke(1.dp, Gray6),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Text(
                            text = when (selectedCategory) {
                                is SavingsType.기타 -> selectedCategory.etc
                                else -> selectedCategory.title
                            },
                            style = TypoTheme.typography.titleLargeM,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCategorySelected(null)
                                }
                                .padding(vertical = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryField(
    onCategorySelected: (SavingsType) -> Unit,
) {
    var interest by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(YearMonth.now()) }
    var period by remember { mutableIntStateOf(0) }
    var etc by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<SavingsType?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.heightIn(max = 300.dp)
        ) {
            items(SavingsType.allTypes) { category ->
                CategoryItem(
                    category = category,
                    selected = selectedCategory == category,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10))
                        .clickable {
                            selectedCategory = category
                            if (category in SavingsType.basicTypes) {
                                onCategorySelected(category)
                            }
                        }
                        .padding(vertical = 20.dp)
                )
            }
        }
        val smartSelectedCategory = selectedCategory
        if (smartSelectedCategory != null) {
            VerticalSpacer(32.dp)
            CategoryAdditionalField(
                visible = smartSelectedCategory is SavingsType.기타,
                title = "기타 저축명 입력",
            ) {
                UnderlineTextField(
                    value = etc,
                    onValueChange = {
                        etc = it
                    },
                    hint = "원하는 저축 종류를 입력하세요",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
            CategoryAdditionalField(
                visible = smartSelectedCategory in SavingsType.interestTypes,
                title = "금리",
            ) {
                Row {
                    UnderlineTextField(
                        value = interest,
                        onValueChange = {
                            val newValue = it.toFloatOrNull()
                            if (newValue != null && newValue in 0f..100f) {
                                interest = it
                            }
                        },
                        hint = "금리을 입력해주세요",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "%",
                            style = TypoTheme.typography.labelLargeM,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }

                }
            }
            CategoryAdditionalField(
                visible = smartSelectedCategory in SavingsType.periodTypes,
                title = "시작일",
            ) {
                YearMonthPicker(
                    onDateSelected = { year, month ->
                        startDate = YearMonth.of(year, month)
                    }
                )
            }
            CategoryAdditionalField(
                visible = smartSelectedCategory in SavingsType.periodTypes,
                title = "만기일",
            ) {
                PeriodPicker(
                    onPeriodSelected = {
                        period = it
                    }
                )
            }
            BottomToTopSlideFadeAnimatedVisibility(
                visible = (smartSelectedCategory in SavingsType.interestTypes && interest.isNotEmpty())
                        || (smartSelectedCategory !in SavingsType.interestTypes && (smartSelectedCategory !is SavingsType.기타 || etc.isNotEmpty()))

            ) {
                RegularButton(
                    text = "다음",
                    onClick = {
                        when (smartSelectedCategory) {
                            SavingsType.보통예금 -> onCategorySelected(SavingsType.보통예금)
                            SavingsType.연금저축 -> onCategorySelected(SavingsType.연금저축)
                            is SavingsType.청약저축 -> onCategorySelected(SavingsType.청약저축(interest))
                            SavingsType.투자 -> onCategorySelected(SavingsType.투자)
                            is SavingsType.기타 -> onCategorySelected(SavingsType.기타(etc))
                            is SavingsType.적금 -> {
                                onCategorySelected(
                                    SavingsType.적금(
                                        interest = interest,
                                        periodStart = startDate,
                                        periodMonth = period
                                    )
                                )
                            }
                            is SavingsType.보험저축 -> {
                                onCategorySelected(
                                    SavingsType.보험저축(
                                        interest = interest,
                                        periodStart = startDate,
                                        periodMonth = period
                                    )
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                )
            }
        }
    }
}

@Composable
private fun CategoryAdditionalField(
    visible: Boolean,
    title: String,
    content: @Composable () -> Unit,
) {
    FadeAnimatedVisibility(visible) {
        Column {
            Text(
                text = title,
                style = TypoTheme.typography.labelLargeM,
            )
            VerticalSpacer(10.dp)
            content()
            VerticalSpacer(32.dp)
        }
    }
}


@Composable
private fun CategoryItem(
    category: SavingsType,
    selected: Boolean,
    modifier: Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceDim,
        border = BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary else Gray6),
        modifier = Modifier
            .padding(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = category.title,
                style = TypoTheme.typography.titleSmallB,
                color = if (selected) White1 else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun SaveCategoriesPreview() {
    JunTheme {
        SaveCategories(
            onCategorySelected = {}
        )
    }
}
package jun.money.mate.spending_plan.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.DefaultTextField
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.TextButton
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.model.spending.SpendingCategory
import jun.money.mate.model.spending.SpendingCategory.Companion.name
import jun.money.mate.model.spending.SpendingCategoryType
import jun.money.mate.model.spending.SpendingType

@Composable
internal fun SpendingPlanAddBody(
    title: String,
    amount: String,
    amountWon: String,
    date: String,
    type: SpendingType,
    spendingCategory: SpendingCategory,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
    onApplyType: (SpendingType) -> Unit,
) {
    val listState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(listState)
            .imePadding()
            .animateContentSize()
    ) {
        SpendingPlanTitle("지출 카테고리") {
            Row {
                SpendingTypeButton(
                    type = SpendingType.ConsumptionPlan,
                    hint = "이번달에 소비할 지출 계획을 설정합니다.",
                    isType = type == SpendingType.ConsumptionPlan,
                    onApplyType = { onApplyType(SpendingType.ConsumptionPlan) },
                    modifier = Modifier.weight(1f)
                )
                HorizontalSpacer(10.dp)
                SpendingTypeButton(
                    type = SpendingType.PredictedSpending,
                    hint = "이번달에 예상되는 지출 금액을 설정합니다.",
                    isType = type == SpendingType.PredictedSpending,
                    onApplyType = { onApplyType(SpendingType.PredictedSpending) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        SpendingPlanTitle(
            "지출 카테고리",
            visible = type == SpendingType.PredictedSpending
        ) {
            TextButton(
                text = spendingCategory.name(),
                onClick = onShowCategoryBottomSheet,
                icon = {
                    if (spendingCategory is SpendingCategory.CategoryType) {
                        CategoryIcon(
                            category = spendingCategory.type
                        )
                    }
                }
            )
        }

        SpendingPlanTitle(
            "지출 예정 날짜",
            visible = type == SpendingType.PredictedSpending
        ) {
            TextButton(
                text = date,
                onClick = onShowDateBottomSheet
            )
        }
        SpendingPlanTitle("지출명") {
            DefaultTextField(
                value = title,
                onValueChange = onTitleChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                hint = "지출명을 입력해주세요"
            )
        }
        SpendingPlanTitle("지출 금액") {
            DefaultTextField(
                value = amount,
                onValueChange = onAmountChange,
                hint = "지출 금액을 입력해주세요",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.NumberPassword
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        onShowCategoryBottomSheet()
                    }
                )
            )
            TopToBottomAnimatedVisibility(amount.isNotEmpty()) {
                Text(
                    text = amountWon,
                    style = JUNTheme.typography.labelLargeM,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SpendingPlanTitle(
    title: String,
    visible: Boolean = true,
    content: @Composable () -> Unit
) {
    if (visible) {
        Column {
            VerticalSpacer(20.dp)
            Text(
                text = title,
                style = JUNTheme.typography.titleMediumM,
            )
            VerticalSpacer(10.dp)
            content()
    }
    }
}

@Composable
private fun SpendingTypeButton(
    type: SpendingType,
    hint: String,
    isType: Boolean,
    onApplyType: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        text = type.title,
        text2 = hint,
        onClick = onApplyType,
        color = if (isType) Red3 else MaterialTheme.colorScheme.surfaceDim,
        textColor = if (isType) White1 else MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
private fun SpendingAddBodyPreview() {
    JunTheme() {
        SpendingPlanAddBody(
            title = "지출명",
            amount = "10000",
            amountWon = "10,000원",
            date = "1일",
            type = SpendingType.ConsumptionPlan,
            spendingCategory = SpendingCategory.CategoryType(SpendingCategoryType.교통비),
            onTitleChange = {},
            onAmountChange = {},
            onShowDateBottomSheet = {},
            onShowCategoryBottomSheet = {},
            onApplyType = {}
        )
    }
}
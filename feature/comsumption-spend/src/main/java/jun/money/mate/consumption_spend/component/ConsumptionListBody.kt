package jun.money.mate.consumption_spend.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray10
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.model.consumption.Consumption
import jun.money.mate.model.consumption.ConsumptionList
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ConsumptionListBody(
    filterValue: String,
    consumptionList: ConsumptionList,
    onConsumptionAdd: () -> Unit,
    onFilterClick: () -> Unit,
    onConsumptionClick: (Consumption) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        item {
            Surface(
                color = MaterialTheme.colorScheme.surfaceDim,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    Text(
                        text = "전체 계획 소비",
                        style = JUNTheme.typography.titleLargeM,
                    )
                    VerticalSpacer(10.dp)
                    Text(
                        text = consumptionList.totalString,
                        style = JUNTheme.typography.headlineSmallB,
                    )
                }
            }
        }

        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = onFilterClick)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = filterValue,
                        style = JUNTheme.typography.titleMediumR,
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .clickable(onClick = onConsumptionAdd)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .align(Alignment.CenterEnd)
                )
            }
        }

        consumptionList.consumptionsGroup.forEach { consumption ->
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                ) {
                    Row {
                        Text(
                            text = consumption.date,
                            style = JUNTheme.typography.titleSmallR,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = consumption.totalString,
                            style = JUNTheme.typography.titleSmallR,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    VerticalSpacer(5.dp)
                    HorizontalDivider()
                }
            }

            items(consumption.consumptions) { consump ->
                ConsumptionItem(
                    consumption = consump,
                    modifier = Modifier
                        .background(
                            if (consump.selected)
                                Gray10
                            else
                                MaterialTheme.colorScheme.surfaceDim
                        )
                        .clickable { onConsumptionClick(consump) }
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                )
            }

            item {
                VerticalSpacer(20.dp)
            }
        }
    }
}

@Composable
private fun ConsumptionItem(
    consumption: Consumption,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row {
            Text(
                text = consumption.title,
                style = JUNTheme.typography.titleMediumR,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = consumption.amountString,
                color = Red3,
                style = JUNTheme.typography.titleMediumR,
            )
        }
        Text(
            text = consumption.planTitle,
            style = JUNTheme.typography.titleSmallR,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SpendingPlanListBodyPreview() {
    JunTheme {
        ConsumptionListBody(
            filterValue = "전체",
            consumptionList = ConsumptionList(
                consumptions = listOf(
                    Consumption(
                        id = 1,
                        title = "맥도날드",
                        amount = 10000,
                        consumptionDate = LocalDate.now(),
                        planTitle = "식비"
                    ),
                    Consumption(
                        id = 2,
                        title = "스타벅스",
                        amount = 5000,
                        consumptionDate = LocalDate.now(),
                        planTitle = "식비"
                    ),
                    Consumption(
                        id = 3,
                        title = "편의점",
                        amount = 3000,
                        consumptionDate = LocalDate.now().plusDays(1),
                        planTitle = "식비"
                    ),
                )
            ),
            onFilterClick = {},
            onConsumptionAdd = {},
            onConsumptionClick = {},
        )
    }
}
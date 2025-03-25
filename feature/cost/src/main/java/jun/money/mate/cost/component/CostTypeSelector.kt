package jun.money.mate.cost.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.ScrollableTab
import jun.money.mate.designsystem.component.UnderlineTextField
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.model.spending.CostType
import jun.money.mate.model.spending.CostType.Companion.name
import jun.money.mate.model.spending.NormalType
import jun.money.mate.model.spending.SubscriptionType
import jun.money.mate.utils.toImageRes

@Composable
internal fun CostTypeSelector(
    onSelected: (CostType?) -> Unit,
    modifier: Modifier = Modifier,
    costType: CostType? = null,
) {
    Crossfade(
        targetState = costType == null,
        modifier = modifier.padding(10.dp)
    ) {
        when (it) {
            true -> {
                CategoryField(
                    onSelected = onSelected,
                )
            }

            false -> {
                if (costType != null) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        shadowElevation = 4.dp,
                        border = BorderStroke(1.dp, Gray6),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Text(
                            text = costType.name,
                            style = TypoTheme.typography.titleLargeM,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelected(null)
                                }
                                .padding(vertical = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

private enum class CostTypeTab {
    일반,
    구독,
    기타
}

@Composable
private fun CategoryField(
    onSelected: (CostType?) -> Unit,
    costType: CostType? = null,
) {
    var selectedTab by remember { mutableStateOf(CostTypeTab.일반) }
    var etcValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ScrollableTab(
            tabs = CostTypeTab.entries.map { it.name },
            selectedTabIndex = selectedTab.ordinal,
            onTabClick = {
                selectedTab = CostTypeTab.entries[it]
             },
        )
        VerticalSpacer(10.dp)
        LazyVerticalGrid(
            columns = GridCells.Fixed(
                if (selectedTab == CostTypeTab.일반) 4 else 3
            ),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 1000.dp)
        ) {
            when (selectedTab) {
                CostTypeTab.일반 -> {
                    items(NormalType.entries) {
                        NormalItem(
                            name = it.name,
                            selected = costType is CostType.Normal && costType.normalType == it,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    onSelected(CostType.Normal(it))
                                }
                                .padding(vertical = 16.dp)
                        )
                    }
                }
                CostTypeTab.구독 -> {
                    items(SubscriptionType.entries) {
                        SubscriptionTypeItem(
                            subscriptionType = it,
                            selected = costType is CostType.Subscription && costType.subscriptionType == it,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    onSelected(CostType.Subscription(it))
                                }
                                .padding(vertical = 16.dp)
                        )
                    }
                }
                CostTypeTab.기타 -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            VerticalSpacer(16.dp)
                            UnderlineTextField(
                                value = etcValue,
                                onValueChange = {
                                    etcValue = it
                                },
                                hint = "소비를 설명해 주세요",
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        onSelected(CostType.Etc(etcValue))
                                    }
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                            VerticalSpacer(10.dp)
                            RegularButton(
                                text = "완료",
                                onClick = {
                                    onSelected(CostType.Etc(etcValue))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NormalItem(
    name: String,
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
                text = name ,
                style = TypoTheme.typography.titleSmallB,
                color = if (selected) White1 else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun SubscriptionTypeItem(
    subscriptionType: SubscriptionType,
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
            Icon(
                painter = painterResource(subscriptionType.toImageRes()),
                tint = Color.Unspecified,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            VerticalSpacer(8.dp)
            Text(
                text = subscriptionType.name,
                style = TypoTheme.typography.labelLargeB,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun CosTypeButtonPreview() {
    JunTheme {
        CostTypeSelector(
            onSelected = {}
        )
    }
}
package jun.money.mate.challenge.contract

import androidx.compose.runtime.Stable
import jun.money.mate.model.save.ChallengeType
import jun.money.mate.utils.currency.CurrencyFormatter

@Stable
internal data class ChallengeAddState(
    val title: String = "",
    val goalAmount: Long = 0,
    val amount: String = "",
    val count: String = "",
    val challengeType: ChallengeType? = null,
) {

    val goalAmountString get() = if (goalAmount > 0) goalAmount.toString() else ""
    val goalAmountWon get() = if (goalAmount > 0) CurrencyFormatter.formatAmountWon(goalAmount) else ""
}

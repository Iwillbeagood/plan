package jun.money.mate.model

import java.text.DecimalFormat

object Utils {

    private const val DECIMAL_PATTERN = "#,###"
    private const val CURRENCY_UNIT = "원"

    fun formatAmountWon(amount: Int?): String {
        if (amount == null) return "0$CURRENCY_UNIT"

        val formatter = DecimalFormat(DECIMAL_PATTERN)
        return "${formatter.format(amount)}$CURRENCY_UNIT"
    }

    fun formatAmountWon(amount: Double?): String {
        if (amount == null) return "0$CURRENCY_UNIT"

        val formatter = DecimalFormat(DECIMAL_PATTERN)
        return "${formatter.format(amount)}$CURRENCY_UNIT"
    }
}
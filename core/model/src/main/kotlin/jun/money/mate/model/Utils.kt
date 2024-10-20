package jun.money.mate.model

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    fun formatDateToKorean(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MM월 dd일")
        return date.format(formatter)
    }

}
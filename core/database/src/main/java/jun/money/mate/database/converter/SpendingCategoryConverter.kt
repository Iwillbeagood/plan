package jun.money.mate.database.converter

import androidx.room.TypeConverter
import jun.money.mate.model.spending.SpendingCategory
import jun.money.mate.model.spending.SpendingCategoryType

class SpendingCategoryConverter {

    @TypeConverter
    fun fromSpendingCategory(spendingCategory: SpendingCategory): String {
        return when (spendingCategory) {
            is SpendingCategory.NotSelected -> SpendingCategory.NOT_SELECTED
            is SpendingCategory.CategoryType -> spendingCategory.type.name
        }
    }

    @TypeConverter
    fun toSpendingCategory(value: String): SpendingCategory {
        return when (value) {
            SpendingCategory.NOT_SELECTED -> SpendingCategory.NotSelected
            else -> {
                val type = SpendingCategoryType.entries.find { it.name == value }
                    ?: SpendingCategoryType.기타
                SpendingCategory.CategoryType(type)
            }
        }
    }
}

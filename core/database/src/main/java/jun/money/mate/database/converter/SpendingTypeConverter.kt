package jun.money.mate.database.converter

import androidx.room.TypeConverter
import jun.money.mate.model.spending.SpendingType

class SpendingTypeConverter {

    @TypeConverter
    fun fromSpendingType(spendingType: SpendingType): String {
        return spendingType.name
    }

    @TypeConverter
    fun toSpendingType(value: String): SpendingType {
        return SpendingType.valueOf(value)
    }
}

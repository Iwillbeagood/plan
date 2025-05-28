package jun.money.mate.database.converter

import androidx.room.TypeConverter
import jun.money.mate.model.save.SavingsType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class SaveCategoryConverter {

    @TypeConverter
    fun fromSaveCategory(savingsType: SavingsType): String {
        return Json.encodeToString(savingsType)
    }

    @TypeConverter
    fun toSaveCategory(value: String): SavingsType {
        return Json.decodeFromString(value)
    }
}

package jun.money.mate.database.converter

import androidx.room.TypeConverter
import jun.money.mate.model.save.SaveCategory

internal class SaveCategoryConverter {

    @TypeConverter
    fun fromSaveCategory(saveCategory: SaveCategory): String {
        return saveCategory.name
    }

    @TypeConverter
    fun toSaveCategory(value: String): SaveCategory {
        return SaveCategory.valueOf(value)
    }
}

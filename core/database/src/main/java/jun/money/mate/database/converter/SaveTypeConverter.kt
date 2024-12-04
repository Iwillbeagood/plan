package jun.money.mate.database.converter

import androidx.room.TypeConverter
import jun.money.mate.model.save.SaveCategory
import jun.money.mate.model.save.SaveType

internal class SaveTypeConverter {

    @TypeConverter
    fun fromSaveType(saveType: SaveType): String {
        return saveType.name
    }

    @TypeConverter
    fun toSaveType(value: String): SaveType {
        return SaveType.valueOf(value)
    }
}

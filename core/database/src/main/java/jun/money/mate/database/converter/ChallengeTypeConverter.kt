package jun.money.mate.database.converter

import androidx.room.TypeConverter
import jun.money.mate.model.save.ChallengeType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChallengeTypeConverter {
    private val json = Json { encodeDefaults = true }

    @TypeConverter
    fun fromChallengeType(value: ChallengeType): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toChallengeType(value: String): ChallengeType {
        return json.decodeFromString(value)
    }
}

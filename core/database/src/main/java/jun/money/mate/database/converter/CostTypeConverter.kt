package jun.money.mate.database.converter

import androidx.room.TypeConverter
import jun.money.mate.model.spending.CostType
import jun.money.mate.model.spending.NormalType
import jun.money.mate.model.spending.SubscriptionType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CostTypeConverter {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromCostType(costType: CostType): String {
        return json.encodeToString(costType)
    }

    @TypeConverter
    fun toCostType(value: String): CostType {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromNormalType(value: NormalType): String {
        return value.name
    }

    @TypeConverter
    fun toNormalType(value: String): NormalType {
        return NormalType.valueOf(value)
    }

    @TypeConverter
    fun fromSubscriptionType(value: SubscriptionType): String {
        return value.name
    }

    @TypeConverter
    fun toSubscriptionType(value: String): SubscriptionType {
        return SubscriptionType.valueOf(value)
    }
}

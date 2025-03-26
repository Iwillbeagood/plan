package jun.money.mate.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jun.money.mate.database.AppDatabase
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.spending.CostType

@Entity(tableName = AppDatabase.COST_TABLE_NAME)
data class CostEntity(
    @PrimaryKey val id: Long,
    val amount: Long,
    val type: CostType,
    val dateType: DateType,
)
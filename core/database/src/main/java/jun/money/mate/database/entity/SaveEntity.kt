package jun.money.mate.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jun.money.mate.database.AppDatabase
import jun.money.mate.model.save.SavingsType

@Entity(tableName = AppDatabase.SAVING_PLAN_TABLE_NAME)
data class SaveEntity(
    @PrimaryKey val id: Long,
    val amount: Long,
    val planDay: Int,
    val savingsType: SavingsType,
    val executeMonth: Int,
    val executed: Boolean,
)
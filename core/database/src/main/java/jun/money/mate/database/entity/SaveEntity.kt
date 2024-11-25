package jun.money.mate.database.entity

import androidx.room.Entity
import jun.money.mate.database.AppDatabase

@Entity(tableName = AppDatabase.SAVING_PLAN_TABLE_NAME)
data class SaveEntity(
    val id: Long,
    val title: String,
    val amount: Long,
    val planDay: Int,
    val executeMonth: Int,
    val executed: Boolean
)
package jun.money.mate.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import jun.money.mate.database.AppDatabase
import java.time.LocalDate

@Entity(
    tableName = AppDatabase.CONSUMPTION_TABLE_NAME,
    indices = [Index(value = ["planTitle"])]
)
data class ConsumptionEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val amount: Long,
    val date: LocalDate,
    val planTitle: String
)

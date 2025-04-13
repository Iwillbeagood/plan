package jun.money.mate.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.util.TableInfo
import jun.money.mate.database.AppDatabase
import jun.money.mate.model.consumption.PastBudget
import java.time.LocalDate
import java.time.YearMonth

@Entity(
    tableName = AppDatabase.BUDGET_TABLE_NAME
)
data class BudgetEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val budget: Long
)

@Entity(
    tableName = AppDatabase.BUDGET_USED_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["budgetId"])]
)
data class UsedEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val budgetId: Long,
    val title: String,
    val amount: Long,
    val date: LocalDate,
)

@Entity(
    tableName = AppDatabase.BUDGET_PAST_BUDGET_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["budgetId"])]
)
data class PastBudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val budgetId: Long,
    val budget: Long,
    val amountUsed: Long,
    val date: YearMonth,
)

data class BudgetWithUsed(
    @Embedded val budget: BudgetEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "budgetId"
    )
    val usedList: List<UsedEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "budgetId"
    )
    val pastBudgets: List<PastBudgetEntity>
)
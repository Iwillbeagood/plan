package jun.money.mate.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import jun.money.mate.database.AppDatabase
import jun.money.mate.database.AppDatabase.Companion.CHALLENGE_PROGRESS_TABLE_NAME
import jun.money.mate.model.save.ChallengeType
import java.time.LocalDate

@Entity(tableName = AppDatabase.CHALLENGE_TABLE_NAME)
data class ChallengeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val count: Int,
    val startDate: LocalDate,
    val goalAmount: Long,
    val type: ChallengeType,
)

@Entity(
    tableName = CHALLENGE_PROGRESS_TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = ChallengeEntity::class,
        parentColumns = ["id"],
        childColumns = ["challengeId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["challengeId"])]
)
data class ChallengeProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val index: Int,
    val challengeId: Long,
    val amount: Long,
    val isAchieved: Boolean,
    val date: LocalDate
)

data class ChallengeWithProgress(
    @Embedded val challenge: ChallengeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "challengeId"
    )
    val progressList: List<ChallengeProgressEntity>
)
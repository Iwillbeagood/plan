package jun.money.mate.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jun.money.mate.database.AppDatabase
import java.time.LocalDateTime

@Entity(
    tableName = AppDatabase.NOTICE_POP_UP_TABLE_NAME
)
data class NoticePopUpEntity(
    @PrimaryKey val id: Int = 1,
    val popUpUrl: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)
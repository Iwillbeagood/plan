package jun.money.mate.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jun.money.mate.database.AppDatabase
import java.time.LocalDateTime

@Entity(tableName = AppDatabase.PUSH_TABLE_NAME)
data class PushEntity(
    @PrimaryKey val id: String,
    val fcmType: Int,
    val title: String,
    val content: String,
    val registeredDate: LocalDateTime,
    val isRead: Boolean = false
)
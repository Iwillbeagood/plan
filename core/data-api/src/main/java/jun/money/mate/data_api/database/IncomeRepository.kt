package jun.money.mate.data_api.database

import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IncomeRepository {

    suspend fun upsertIncome(income: Income)

    fun getIncomeFlow(): Flow<IncomeList>

    suspend fun getIncomeById(
        id: Long
    ): Income

    fun getIncomesByMonth(
        data: LocalDate = LocalDate.now()
    ): Flow<IncomeList>

    suspend fun deleteById(id: Long)

    suspend fun deleteByIds(ids: List<Long>)
}
package jun.money.mate.data_api.database

import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList
import kotlinx.coroutines.flow.Flow

interface IncomeRepository {

    suspend fun upsertIncome(income: Income)

    fun getIncomeFlow(): Flow<IncomeList>

    fun getIncomesByMonth(year: String, month: String): Flow<IncomeList>

    suspend fun deleteById(id: Long)
}
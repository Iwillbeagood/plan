package jun.money.mate.dataApi.database

import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface IncomeRepository {

    suspend fun insertIncome(income: Income)

    suspend fun updateIncome(income: Income)

    fun getIncomeFlow(): Flow<IncomeList>

    suspend fun getIncomeById(
        id: Long,
    ): Income

    fun getIncomesByMonth(
        data: YearMonth = YearMonth.now(),
    ): Flow<IncomeList>

    suspend fun deleteById(id: Long)

    suspend fun deleteByIds(ids: List<Long>)
}

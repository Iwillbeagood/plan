package jun.money.mate.data.resository

import jun.money.mate.data.mapper.toIncome
import jun.money.mate.data.mapper.toIncomeEntity
import jun.money.mate.data.mapper.toIncomeList
import jun.money.mate.dataApi.database.IncomeRepository
import jun.money.mate.database.dao.IncomeDao
import jun.money.mate.database.entity.IncomeEntity
import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList
import jun.money.mate.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import javax.inject.Inject

class IncomeRepositoryImpl @Inject constructor(
    private val incomeDao: IncomeDao,
) : IncomeRepository {

    override suspend fun insertIncome(income: Income) {
        try {
            incomeDao.insertIncome(income.toIncomeEntity())
        } catch (e: Exception) {
            Logger.e("upsertIncome error: $e")
        }
    }

    override suspend fun updateIncome(income: Income) {
        try {
            incomeDao.updateIncome(income.toIncomeEntity())
        } catch (e: Exception) {
            Logger.e("updateIncome error: $e")
        }
    }

    override fun getIncomeFlow(): Flow<IncomeList> {
        return incomeDao.getIncomeFlow()
            .map(List<IncomeEntity>::toIncomeList)
            .catch {
                Logger.e("getIncomeFlow error: $it")
            }
    }

    override suspend fun getIncomeById(id: Long): Income {
        return incomeDao.getIncomeById(id).toIncome()
    }

    override fun getIncomesByMonth(data: YearMonth): Flow<IncomeList> {
        return incomeDao.getIncomeFlow()
            .map {
                it.filter { income ->
                    YearMonth.from(income.addDate) == data
                }
            }
            .map(List<IncomeEntity>::toIncomeList)
            .catch {
                Logger.e("getIncomesByMonth error: $it")
            }
    }

    override suspend fun deleteById(id: Long) {
        try {
            incomeDao.deleteById(id)
        } catch (e: Exception) {
            Logger.e("deleteById error: $e")
        }
    }

    override suspend fun deleteByIds(ids: List<Long>) {
        try {
            incomeDao.deleteByIds(ids)
        } catch (e: Exception) {
            Logger.e("deleteByIds error: $e")
        }
    }
}

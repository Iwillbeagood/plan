package jun.money.mate.data.resository

import jun.money.mate.data_api.database.IncomeRepository
import jun.money.mate.database.dao.IncomeDao
import jun.money.mate.database.entity.IncomeEntity
import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList
import kic.owner2.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class IncomeRepositoryImpl @Inject constructor(
    private val incomeDao: IncomeDao
) : IncomeRepository {

    override suspend fun upsertIncome(income: Income) {
        try {
            incomeDao.upsertIncome(
                IncomeEntity(
                    title = income.title,
                    amount = income.amount,
                    type = income.type,
                    incomeDate = income.incomeDate,
                )
            )
        } catch (e: Exception) {
            Logger.e("upsertIncome error: $e")
        }
    }

    override fun getIncomeFlow(): Flow<IncomeList> {
        return incomeDao.getIncomeFlow().map { list ->
            IncomeList(
                list.map {
                    Income(
                        id = it.id,
                        title = it.title,
                        amount = it.amount,
                        type = it.type,
                        incomeDate = it.incomeDate,
                    )
                }
            )
        }
    }

    override fun getIncomesByMonth(data: LocalDate): Flow<IncomeList> {
        return incomeDao.getIncomesByMonth(
            year = data.year.toString(),
            month = data.monthValue.toString()
        ).map { list ->
            IncomeList(
                list.map {
                    Income(
                        id = it.id,
                        title = it.title,
                        amount = it.amount,
                        type = it.type,
                        incomeDate = it.incomeDate,
                    )
                }
            )
        }
    }

    override suspend fun deleteById(id: Long) {
        try {
            incomeDao.deleteById(id)
        } catch (e: Exception) {
            Logger.e("deleteById error: $e")
        }
    }
}
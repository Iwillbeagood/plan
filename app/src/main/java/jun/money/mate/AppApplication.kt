package jun.money.mate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import jun.money.mate.workmanager.scheduleBudgetMonthlyWork
import jun.money.mate.workmanager.scheduleSavingMonthlyWork

@HiltAndroidApp
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        scheduleSavingMonthlyWork(this)
        scheduleBudgetMonthlyWork(this)
    }
}
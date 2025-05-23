package jun.money.mate

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.testing.TestListenableWorkerBuilder
import jun.money.mate.workmanager.IncomeMonthlyWorker
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IncomeMonthlyWorkerTest {

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val worker = TestListenableWorkerBuilder<IncomeMonthlyWorker>(context = context).build()
    }
}
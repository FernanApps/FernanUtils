package pe.fernan.utils.test

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

abstract class BaseTesting {

    fun launchActivity(cls: Class<*>): Activity {
        return launchActivity<Activity>(cls as Class<Activity>)
    }

    fun launchFragment(cls: Class<*>): Fragment {
        return launchFragment<Fragment>(cls as Class<Fragment>)
    }

    fun sleep(timeSeconds: Int = 15) {
        sleep(timeSeconds.toLong())
    }

    companion object {
        inline fun <reified T : Fragment> launchFragment(cls: Class<T>): T {
            val fragmentScenario = FragmentScenario.launchInContainer(cls)
            var fragment: T? = null
            fragmentScenario.onFragment {
                fragment = it
            }
            return fragment!!
        }


        inline fun <reified T : Activity> launchActivity(cls: Class<T>): T {
            val context: Context = ApplicationProvider.getApplicationContext()
            val intent = Intent(context, cls)
            val scenario = ActivityScenario.launch<T>(intent)

            var activity: T? = null
            scenario.onActivity {
                activity = it
            }

            return activity!!
        }


        @JvmStatic
        fun launchActivityJava(cls: Class<*>): Activity {
            return launchActivity<Activity>(cls as Class<Activity>)
        }


        @JvmStatic
        fun sleep(timeSeconds: Long = 15) {
            try {
                TimeUnit.SECONDS.sleep(timeSeconds)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }

        fun <R> runOnUiThread(activity: Activity?, block: Activity.() -> R) {
            checkNotNull(activity)
            activity.runOnUiThread {
                activity.block()
            }
        }

        fun <R> sleepAndRunOnUiThread(
            activity: Activity?,
            time: Long = 5,
            block: Activity.() -> R,
        ) {
            checkNotNull(activity)
            thread {
                TimeUnit.SECONDS.sleep(time)
                activity.runOnUiThread {
                    activity.block()
                }
            }
        }

    }
}
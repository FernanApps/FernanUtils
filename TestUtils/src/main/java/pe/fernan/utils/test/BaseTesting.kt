package pe.fernan.utils.test

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import java.io.Serializable
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


abstract class BaseTesting {
    val context: Context get() = ApplicationProvider.getApplicationContext()

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
            return launchActivity<T>(cls, *emptyArray())
        }

        inline fun <reified T : Activity> launchActivity(cls: Class<T>, vararg data: Pair<String, Any>): T {
            val context: Context = ApplicationProvider.getApplicationContext()
            val intent = addDataToIntent(Intent(context, cls), *data)
            return launchActivity<T>(intent)
        }

        inline fun <reified T : Activity> launchActivity(intent: Intent): T {
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


        fun addDataToIntent(intent: Intent, vararg data: Pair<String, Any>): Intent {

            with(intent){
                data.forEach { pair ->
                    val key = pair.first
                    val value = pair.second
                    when (value) {
                        is String -> putExtra(key, value)
                        is Int -> putExtra(key, value)
                        is Long -> putExtra(key, value)
                        is Float -> putExtra(key, value)
                        is Double -> putExtra(key, value)
                        is Boolean -> putExtra(key, value)
                        is Char -> putExtra(key, value)
                        is Byte -> putExtra(key, value)
                        is Short -> putExtra(key, value)
                        is Parcelable -> putExtra(key, value)
                        is Serializable -> putExtra(key, value)
                        is Array<*> -> {
                            when {
                                value.isArrayOf<String>() -> putExtra(key, value as Array<String>)
                                value.isArrayOf<Int>() -> putExtra(key, value as Array<Int>)
                                value.isArrayOf<Long>() -> putExtra(key, value as Array<Long>)
                                value.isArrayOf<Float>() -> putExtra(key, value as Array<Float>)
                                value.isArrayOf<Double>() -> putExtra(key, value as Array<Double>)
                                value.isArrayOf<Boolean>() -> putExtra(key, value as Array<Boolean>)
                                value.isArrayOf<Char>() -> putExtra(key, value as Array<Char>)
                                value.isArrayOf<Byte>() -> putExtra(key, value as Array<Byte>)
                                value.isArrayOf<Short>() -> putExtra(key, value as Array<Short>)
                                // Agrega más tipos de arrays según sea necesario
                                else -> throw IllegalArgumentException("Tipo de array no soportado: ${value.javaClass.simpleName}")
                            }
                        }
                        else -> throw IllegalArgumentException("Tipo de dato no soportado: ${value.javaClass.simpleName}")
                    }
                }
            }

            return intent
        }

    }
}
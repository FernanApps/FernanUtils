package pe.fernan.demo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import pe.fernan.utils.test.BaseTesting

@RunWith(AndroidJUnit4::class)
class MainActivityTest : BaseTesting(){

    @Test
    fun openActivity(){
        launchActivity(MainActivity::class.java)
        sleep(30)
    }
}
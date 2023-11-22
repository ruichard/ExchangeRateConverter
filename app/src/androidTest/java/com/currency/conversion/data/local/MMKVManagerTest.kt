package com.currency.conversion.data.local

import androidx.test.core.app.ApplicationProvider
import com.exchangerate.converter.data.local.MMKVManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MMKVManagerTest {

    private lateinit var mmkvManager: MMKVManager

    @Before
    fun setup() {
        mmkvManager = MMKVManager(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testPutAndGetLong() {
        mmkvManager.putLong("key", 10L)
        Assert.assertEquals(10L, mmkvManager.getLong("key", 0L))
    }

    @Test
    fun testPutAndGetObject() {
        val obj = Person("Ruichard", 30)
        mmkvManager.putObject("obj", obj)

        val person = mmkvManager.getObject<Person>("obj")
        Assert.assertEquals(obj.name, person!!.name)
        Assert.assertEquals(obj.age, person.age)
    }

}

data class Person(val name: String, val age: Int)

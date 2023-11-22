package com.exchangerate.converter.data.local

import android.content.Context
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MMKVManager @Inject constructor(@ApplicationContext context: Context) {

    init {
        MMKV.initialize(context)
    }

    fun putObject(key: String, obj: Any) {
        MMKV.defaultMMKV().encode(key, Gson().toJson(obj))
    }

    fun putLong(key: String, value: Long) {
        MMKV.defaultMMKV().encode(key, value)
    }

    inline fun <reified T> getObject(key: String): T? {
        val json = MMKV.defaultMMKV().decodeString(key)
        return Gson().fromJson(json, T::class.java)
    }

    fun getLong(key: String, defaultValue: Long = 0L) = MMKV.defaultMMKV().decodeLong(key, defaultValue)

}

package com.exchangerate.converter.data.local

import com.exchangerate.converter.data.model.ExchangeRate
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getExchangeRate(): Flow<ExchangeRate?>
    suspend fun saveExchangeRate(rates: ExchangeRate)
    suspend fun saveLastUpdateTime(time: Long)
    fun getLastUpdateTime(): Flow<Long>
}
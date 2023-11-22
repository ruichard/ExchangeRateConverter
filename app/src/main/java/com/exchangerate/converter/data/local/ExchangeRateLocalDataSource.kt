package com.exchangerate.converter.data.local

import com.exchangerate.converter.util.Constants
import com.exchangerate.converter.data.model.ExchangeRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateLocalDataSource @Inject constructor(private val mmkvManager: MMKVManager) :
    LocalDataSource {

    override fun getExchangeRate(): Flow<ExchangeRate?> =
        flow { emit(mmkvManager.getObject<ExchangeRate>(Constants.MMKV_EXCHANGE_RATE)) }

    override suspend fun saveExchangeRate(rates: ExchangeRate) {
        mmkvManager.putObject(Constants.MMKV_EXCHANGE_RATE, rates)
    }

    override suspend fun saveLastUpdateTime(time: Long) {
        mmkvManager.putLong(Constants.MMKV_LAST_UPDATE_TIME, time)
    }

    override fun getLastUpdateTime(): Flow<Long> =
        flow { emit(mmkvManager.getLong(Constants.MMKV_LAST_UPDATE_TIME)) }
}
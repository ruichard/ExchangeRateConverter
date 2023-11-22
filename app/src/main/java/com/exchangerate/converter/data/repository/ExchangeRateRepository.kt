package com.exchangerate.converter.data.repository

import com.exchangerate.converter.data.local.LocalDataSource
import com.exchangerate.converter.data.model.ExchangeRate
import com.exchangerate.converter.data.remote.RemoteDataSource
import com.exchangerate.converter.data.remote.model.ExchangeRateResponse
import com.exchangerate.converter.data.remote.model.asEntity
import com.exchangerate.converter.util.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ExchangeRateRepository @Inject constructor(
    private val updateChecker: UpdateChecker,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val networkMonitor: NetworkMonitor,
) {

    fun getLatestRates(): Flow<ExchangeRate> = flow {
        if (updateChecker.shouldUpdateData() && networkMonitor.isConnected()) {
            remoteDataSource.getExchangeRate()
                .map { response -> handleResponse(response) }
                .collect { exchangeRate -> emit(exchangeRate) }
        } else {
            localDataSource.getExchangeRate()
                .collect { exchangeRate ->
                    exchangeRate?.let { emit(it) } ?: throw Exception("No data available locally")
                }
        }
    }

    private suspend fun handleResponse(response: Response<ExchangeRateResponse>): ExchangeRate {
        if (response.isSuccessful) {
            response.body()?.let {
                val exchangeRate = it.asEntity()
                localDataSource.run {
                    saveExchangeRate(exchangeRate)
                    saveLastUpdateTime(System.currentTimeMillis())
                }
                return exchangeRate
            } ?: throw Exception("Response body is null")
        } else {
            throw Exception("Response error: ${response.errorBody()}")
        }
    }

}
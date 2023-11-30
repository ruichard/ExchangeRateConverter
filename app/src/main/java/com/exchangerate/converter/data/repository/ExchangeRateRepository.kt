package com.exchangerate.converter.data.repository

import android.util.Log
import com.exchangerate.converter.data.local.LocalDataSource
import com.exchangerate.converter.data.model.ExchangeRate
import com.exchangerate.converter.data.remote.RemoteDataSource
import com.exchangerate.converter.data.remote.model.ExchangeRateResponse
import com.exchangerate.converter.data.remote.model.asEntity
import com.exchangerate.converter.util.NetworkMonitor
import com.exchangerate.converter.util.throwCustomException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
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

    suspend fun getLatestRates(): Flow<ExchangeRate> = flow {
        if (updateChecker.shouldUpdateData() && networkMonitor.isConnected()) {
            remoteDataSource.getExchangeRate()
                .map { response -> handleResponse(response) }
                .catch {
                    emit(getDataFromLocal("No data available locally after remote fetch failed: ${it.message}"))
                }
                .collect { exchangeRate -> emit(exchangeRate) }
        } else {
            emit(getDataFromLocal("No data available locally"))
        }
    }

    private suspend fun getDataFromLocal(message: String): ExchangeRate {
        return localDataSource.getExchangeRate().firstOrNull() ?: throw Exception(message)
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
            } ?: throwCustomException("Response body is null")
        } else {
            throwCustomException("Response error: ${response.errorBody()}")
        }
    }

}

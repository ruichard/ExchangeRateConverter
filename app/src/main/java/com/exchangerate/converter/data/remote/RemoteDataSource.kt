package com.exchangerate.converter.data.remote

import com.exchangerate.converter.data.remote.model.ExchangeRateResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {
    fun getExchangeRate(): Flow<Response<ExchangeRateResponse>>
}
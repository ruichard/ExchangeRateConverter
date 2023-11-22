package com.exchangerate.converter.data.remote

import com.exchangerate.converter.data.remote.api.ExchangeRateApiService
import com.exchangerate.converter.data.remote.model.ExchangeRateResponse
import com.exchangerate.converter.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class ExchangeRateRemoteDataSource @Inject constructor(
    private val exchangeRateApiService: ExchangeRateApiService,
) : RemoteDataSource {

    override fun getExchangeRate(): Flow<Response<ExchangeRateResponse>> =
        flow { emit(exchangeRateApiService.getLatestRate(Constants.OPEN_EXCHANGE_API_ID)) }

}
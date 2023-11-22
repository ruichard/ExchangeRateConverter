package com.exchangerate.converter.data.remote.api

import com.exchangerate.converter.data.remote.model.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApiService {

    @GET("latest.json")
    suspend fun getLatestRate(@Query("app_id") appId: String): Response<ExchangeRateResponse>

}

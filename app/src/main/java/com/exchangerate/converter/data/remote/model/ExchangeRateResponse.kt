package com.exchangerate.converter.data.remote.model

import com.exchangerate.converter.data.model.ExchangeRate
import com.squareup.moshi.Json

data class ExchangeRateResponse(
    val base: String,
    val rates: Map<String, Double>,
)

fun ExchangeRateResponse.asEntity() = ExchangeRate(
    rates = rates
)

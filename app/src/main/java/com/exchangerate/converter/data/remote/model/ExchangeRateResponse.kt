package com.exchangerate.converter.data.remote.model

import com.exchangerate.converter.data.model.ExchangeRate
import com.google.gson.annotations.SerializedName
data class ExchangeRateResponse(
    @SerializedName("base")
    val base: String,
    @SerializedName("rates")
    val rates: Map<String, Double>,
)

fun ExchangeRateResponse.asEntity() = ExchangeRate(
    rates = rates
)

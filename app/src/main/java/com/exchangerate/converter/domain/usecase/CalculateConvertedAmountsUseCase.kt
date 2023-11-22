package com.exchangerate.converter.domain.usecase

import com.exchangerate.converter.domain.model.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CalculateConvertedAmountsUseCase @Inject constructor(
    private val getLatestExchangeRatesUseCase: GetLatestExchangeRatesUseCase,
) {
    suspend operator fun invoke(amount: Double, currencyCode: String): Flow<List<Currency>> = flow {
        getLatestExchangeRatesUseCase().collect { exchangeRate ->
            val rates = exchangeRate.rates
            val convertedAmounts = calculate(amount, currencyCode, rates)
            emit(convertedAmounts)
        }
    }

    private fun calculate(
        amount: Double,
        currencyCode: String,
        rates: Map<String, Double>
    ): List<Currency> {
        val baseRate = rates[currencyCode] ?: return emptyList()
        return rates.mapNotNull { (code, rate) ->
            if (code != currencyCode) {
                val convertedAmount = amount * rate / baseRate
                Currency(code, convertedAmount)
            } else {
                null
            }
        }
    }
}

package com.exchangerate.converter.domain.usecase

import com.exchangerate.converter.data.repository.ExchangeRateRepository
import com.exchangerate.converter.data.model.ExchangeRate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestExchangeRatesUseCase @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository
) {
    suspend operator fun invoke(): Flow<ExchangeRate> = exchangeRateRepository.getLatestRates()
}

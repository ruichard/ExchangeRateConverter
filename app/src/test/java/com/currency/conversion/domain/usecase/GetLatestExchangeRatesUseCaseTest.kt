package com.currency.conversion.domain.usecase

import com.exchangerate.converter.data.repository.ExchangeRateRepository
import com.exchangerate.converter.data.model.ExchangeRate
import com.exchangerate.converter.domain.usecase.GetLatestExchangeRatesUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetLatestExchangeRatesUseCaseTest {

    @Mock
    private lateinit var exchangeRateRepository: ExchangeRateRepository

    private lateinit var getLatestExchangeRatesUseCase: GetLatestExchangeRatesUseCase

    @Before
    fun setUp() {
        getLatestExchangeRatesUseCase = GetLatestExchangeRatesUseCase(exchangeRateRepository)
    }

    @Test
    fun testInvoke() = runTest {
        val expectedExchangeRate = Result.success(ExchangeRate(mapOf("USD" to 1.0, "EUR" to 0.85)))
        `when`(exchangeRateRepository.getLatestRates()).thenReturn(expectedExchangeRate)

        val result = getLatestExchangeRatesUseCase.invoke()

        assertEquals(expectedExchangeRate, result)
    }
}

package com.currency.conversion.domain.usecase

import com.exchangerate.converter.domain.model.Currency
import com.exchangerate.converter.data.model.ExchangeRate
import com.exchangerate.converter.domain.usecase.CalculateConvertedAmountsUseCase
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
class CalculateConvertedAmountsUseCaseTest {

    @Mock
    private lateinit var getLatestExchangeRatesUseCase: GetLatestExchangeRatesUseCase

    private lateinit var calculateConvertedAmountsUseCase: CalculateConvertedAmountsUseCase

    @Before
    fun setUp() {
        calculateConvertedAmountsUseCase =
            CalculateConvertedAmountsUseCase(getLatestExchangeRatesUseCase)
    }

    @Test
    fun testInvoke() = runTest {
        val amount = 100.0
        val currencyCode = "USD"
        val exchangeRate = ExchangeRate(mapOf("USD" to 1.0, "EUR" to 0.85))
        val expectedConversions = listOf(
            Currency("USD", 100.0),
            Currency("EUR", 85.0)
        )

        `when`(getLatestExchangeRatesUseCase()).thenReturn(Result.success(exchangeRate))
        `when`(calculateConvertedAmountsUseCase.calculate(amount, currencyCode, exchangeRate.rates)).thenReturn(
            expectedConversions
        )

        val result = calculateConvertedAmountsUseCase(amount, currencyCode)

        assertEquals(expectedConversions, result)
    }

    @Test
    fun testNetworkError() = runTest {
        val amount = 100.0
        val currencyCode = "USD"

        `when`(getLatestExchangeRatesUseCase()).thenReturn(Result.failure(Exception("Network error")))

        val result = calculateConvertedAmountsUseCase(amount, currencyCode)

        assertEquals(emptyList<Currency>(), result)
    }
}

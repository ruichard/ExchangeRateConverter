package com.exchangerate.converter.domain.usecase

import SampleData
import com.exchangerate.converter.data.model.ExchangeRate
import com.exchangerate.converter.domain.model.Currency
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.math.abs

@ExperimentalCoroutinesApi
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
    fun `invoke returns correct converted amounts`() = runTest {
        val amount = 100.0
        val currencyCode = "USD"
        val fakeExchangeRate = ExchangeRate(SampleData.exchangeRateResponse.rates)
        Mockito.`when`(getLatestExchangeRatesUseCase()).thenReturn(flowOf(fakeExchangeRate))

        val result = calculateConvertedAmountsUseCase(amount, currencyCode).toList().first()

        val expected = listOf(
            Currency("AED", 367.2475),
            Currency("JPY", 11000.0),
            Currency("CNY", 780.0),
            Currency("EUR", 85.0)
        )

        val epsilon = 0.0001
        result.forEachIndexed { index, currency ->
            val expectedCurrency = expected[index]
            assert(currency.currencyCode == expectedCurrency.currencyCode)
            assert(abs(currency.amount - expectedCurrency.amount) < epsilon)
        }

    }
}

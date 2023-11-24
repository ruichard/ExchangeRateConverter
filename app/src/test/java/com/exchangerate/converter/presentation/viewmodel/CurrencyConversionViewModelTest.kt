package com.exchangerate.converter.presentation.viewmodel

import com.exchangerate.converter.data.model.ExchangeRate
import com.exchangerate.converter.domain.model.Currency
import com.exchangerate.converter.domain.usecase.CalculateConvertedAmountsUseCase
import com.exchangerate.converter.domain.usecase.GetLatestExchangeRatesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CurrencyConversionViewModelTest {
    @Mock
    private lateinit var getLatestExchangeRatesUseCase: GetLatestExchangeRatesUseCase

    @Mock
    private lateinit var calculateConvertedAmountsUseCase: CalculateConvertedAmountsUseCase

    private lateinit var currencyConversionViewModel: CurrencyConversionViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        currencyConversionViewModel = CurrencyConversionViewModel(
            getLatestExchangeRatesUseCase,
            calculateConvertedAmountsUseCase
        )
    }

    @Test
    fun getCurrencies() = runTest {
        val fakeCurrencies = listOf("USD", "EUR", "JPY")
        val fakeExchangeRate = ExchangeRate(mapOf("USD" to 1.0, "EUR" to 0.85, "JPY" to 110.0))
        `when`(getLatestExchangeRatesUseCase()).thenReturn(flowOf(fakeExchangeRate))

        currencyConversionViewModel.fetchCurrencies()

        advanceUntilIdle()

        val result = currencyConversionViewModel.currencies.first()
        assertEquals(fakeCurrencies, result)
    }

    @Test
    fun getExchangeRateState() = runTest {
        val fakeConvertedAmounts = listOf(Currency("EUR", 85.0))
        `when`(calculateConvertedAmountsUseCase(100.0, "USD"))
            .thenReturn(flowOf(fakeConvertedAmounts))

        currencyConversionViewModel.handleInput("100", "USD")

        advanceUntilIdle()

        val result = currencyConversionViewModel.exchangeRateState.value
        assertEquals(fakeConvertedAmounts, result)
    }

    @Test
    fun handleInput() = runTest {
        val amount = 100.0
        val currencyCode = "USD"
        val fakeConvertedAmounts = listOf(Currency("EUR", 85.0))
        `when`(calculateConvertedAmountsUseCase(amount, currencyCode))
            .thenReturn(flowOf(fakeConvertedAmounts))

        currencyConversionViewModel.handleInput(amount.toString(), currencyCode)
        advanceUntilIdle()

        assert(currencyConversionViewModel.exchangeRateState.value == fakeConvertedAmounts)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
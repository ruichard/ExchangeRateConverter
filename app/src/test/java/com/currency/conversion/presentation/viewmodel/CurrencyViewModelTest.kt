package com.currency.conversion.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.exchangerate.converter.domain.model.Currency
import com.exchangerate.converter.data.model.ExchangeRate
import com.exchangerate.converter.domain.usecase.CalculateConvertedAmountsUseCase
import com.exchangerate.converter.domain.usecase.GetLatestExchangeRatesUseCase
import com.exchangerate.converter.presentation.viewmodel.CurrencyConversionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class CurrencyViewModelTest {

    class CoroutineTestRule : TestRule {
        override fun apply(base: Statement, description: Description) = object : Statement() {
            override fun evaluate() {
                Dispatchers.setMain(Dispatchers.Unconfined)
                try {
                    base.evaluate()
                } finally {
                    Dispatchers.resetMain()
                }
            }
        }
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var getLatestExchangeRatesUseCase: GetLatestExchangeRatesUseCase

    @Mock
    lateinit var calculateConvertedAmountsUseCase: CalculateConvertedAmountsUseCase

    @Mock
    lateinit var currenciesObserver: Observer<List<String>>

    @Mock
    lateinit var convertedAmountsObserver: Observer<List<Currency>>

    @Mock
    lateinit var errorObserver: Observer<String>

    private lateinit var viewModel: CurrencyConversionViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = CurrencyConversionViewModel(getLatestExchangeRatesUseCase, calculateConvertedAmountsUseCase)
        viewModel.currencies.observeForever(currenciesObserver)
        viewModel.exchangeRateState.observeForever(convertedAmountsObserver)
        viewModel.error.observeForever(errorObserver)
    }

    @Test
    fun testFetchCurrencies() = runTest {
        val mockedExchangeRate = ExchangeRate(mapOf("USD" to 1.0, "EUR" to 0.85))
        `when`(getLatestExchangeRatesUseCase()).thenReturn(Result.success(mockedExchangeRate))

        viewModel.fetchCurrencies()

        verify(currenciesObserver).onChanged(listOf("USD", "EUR"))
    }

    @Test
    fun testHandleInput1() = runTest {
        val amountText = "10.0"
        val currencyCode = "USD"
        val mockedConversions = listOf(Currency("EUR", 8.5))
//        `when`(calculateConvertedAmountsUseCase(10.0, "USD")).thenReturn(mockedConversions)

        viewModel.handleInput(amountText, currencyCode)

        verify(convertedAmountsObserver).onChanged(mockedConversions)
    }

    @Test
    fun testHandleInput2() {
        val amountText = "null"
        val currencyCode = "USD"

        viewModel.handleInput(amountText, currencyCode)

        verify(convertedAmountsObserver).onChanged(emptyList())
    }

    @Test
    fun testHandleInput3() {
        val amountText = "-1"
        val currencyCode = "USD"

        viewModel.handleInput(amountText, currencyCode)

        verify(convertedAmountsObserver).onChanged(emptyList())
    }
}

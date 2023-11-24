package com.exchangerate.converter.domain.usecase

import SampleData
import com.exchangerate.converter.data.model.ExchangeRate
import com.exchangerate.converter.data.repository.ExchangeRateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
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
    fun `invoke calls getLatestRates from repository`() = runTest {
        val fakeExchangeRate = ExchangeRate(SampleData.exchangeRateResponse.rates)
        val fakeFlow: Flow<ExchangeRate> = flow { emit(fakeExchangeRate) }
        Mockito.`when`(exchangeRateRepository.getLatestRates()).thenReturn(fakeFlow)

        val resultFlow = getLatestExchangeRatesUseCase.invoke()

        assert(resultFlow == fakeFlow)
    }
}

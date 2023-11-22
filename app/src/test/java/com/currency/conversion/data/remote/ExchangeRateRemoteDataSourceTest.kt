package com.currency.conversion.data.remote

import com.exchangerate.converter.data.remote.api.ExchangeRateApiService
import com.exchangerate.converter.data.model.ExchangeRate
import com.exchangerate.converter.data.remote.ExchangeRateRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ExchangeRateRemoteDataSourceTest {

    @Mock
    private lateinit var exchangeRateApiService: ExchangeRateApiService

    private lateinit var exchangeRateRemoteDataSource: ExchangeRateRemoteDataSource

    @Before
    fun setUp() {
        exchangeRateRemoteDataSource = ExchangeRateRemoteDataSource(exchangeRateApiService)
    }

    @Test
    fun testGetExchangeRate() = runTest {
        val fakeExchangeRate = ExchangeRate(mapOf("USD" to 1.0, "EUR" to 0.85))
        val fakeResponse = Response.success(fakeExchangeRate)
//        `when`(exchangeRateApiService.getLatestRate(anyString())).thenReturn(fakeResponse)
//        val result = exchangeRateRemoteDataSource.getExchangeRate()
//        assertEquals(fakeResponse, result)
    }
}

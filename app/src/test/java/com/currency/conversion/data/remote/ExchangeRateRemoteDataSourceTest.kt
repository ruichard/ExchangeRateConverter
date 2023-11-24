package com.currency.conversion.data.remote

import com.currency.conversion.SampleData
import com.exchangerate.converter.data.remote.ExchangeRateRemoteDataSource
import com.exchangerate.converter.data.remote.api.ExchangeRateApiService
import com.exchangerate.converter.data.remote.model.ExchangeRateResponse
import com.exchangerate.converter.util.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
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
    fun `getExchangeRate returns successful response`() = runTest {
        val mockResponse = Response.success(SampleData.exchangeRateResponse)
        `when`(exchangeRateApiService.getLatestRate(Constants.OPEN_EXCHANGE_API_ID)).thenReturn(
            mockResponse
        )

        val response = exchangeRateRemoteDataSource.getExchangeRate().first()

        assert(response.isSuccessful && response.body() != null)
    }

    @Test
    fun `getExchangeRate handles API failure`() = runTest {
        val errorResponse =
            Response.error<ExchangeRateResponse>(404, "".toResponseBody(null))
        `when`(exchangeRateApiService.getLatestRate(Constants.OPEN_EXCHANGE_API_ID)).thenReturn(
            errorResponse
        )

        val response = exchangeRateRemoteDataSource.getExchangeRate().first()

        assert(!response.isSuccessful)
    }
}

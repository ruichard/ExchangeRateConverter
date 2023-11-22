package com.currency.conversion.data.remote.api

import com.exchangerate.converter.util.Constants
import com.exchangerate.converter.data.remote.api.ExchangeRateApiService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExchangeRateApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ExchangeRateApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ExchangeRateApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetLatestRates() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""{
                "rates": {
                    "USD": 1.0,
                    "EUR": 0.85
                }
            }""")
        mockWebServer.enqueue(mockResponse)

        val response = apiService.getLatestRate(Constants.OPEN_EXCHANGE_API_ID)

        assertEquals(200, response.code())
        val exchangeRate = response.body()
        assertEquals(1.0, exchangeRate?.rates?.get("USD"))
        assertEquals(0.85, exchangeRate?.rates?.get("EUR"))
    }
}
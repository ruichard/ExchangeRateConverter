package com.exchangerate.converter.data.repository

import SampleData
import com.exchangerate.converter.data.local.LocalDataSource
import com.exchangerate.converter.data.model.ExchangeRate
import com.exchangerate.converter.data.remote.RemoteDataSource
import com.exchangerate.converter.data.remote.model.ExchangeRateResponse
import com.exchangerate.converter.util.NetworkMonitor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ExchangeRateRepositoryTest {

    @Mock
    private lateinit var updateChecker: UpdateChecker

    @Mock
    private lateinit var localDataSource: LocalDataSource

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    private lateinit var networkMonitor: NetworkMonitor

    private lateinit var exchangeRateRepository: ExchangeRateRepository

    @Before
    fun setUp() {
        exchangeRateRepository = ExchangeRateRepository(updateChecker, localDataSource, remoteDataSource, networkMonitor)
    }

    @Test
    fun `getLatestRates fetches from remote when should update and network connected`() = runTest {
        val fakeResponse = Response.success(SampleData.exchangeRateResponse)

        Mockito.`when`(updateChecker.shouldUpdateData()).thenReturn(true)
        Mockito.`when`(networkMonitor.isConnected()).thenReturn(true)
        Mockito.`when`(remoteDataSource.getExchangeRate()).thenReturn(flowOf(fakeResponse))

        val exchangeRate = exchangeRateRepository.getLatestRates().first()

        assertEquals(ExchangeRate(SampleData.exchangeRateResponse.rates), exchangeRate)
    }
    @Test
    fun `getLatestRates handles remote data fetch failure`() = runTest {
        Mockito.`when`(updateChecker.shouldUpdateData()).thenReturn(true)
        Mockito.`when`(networkMonitor.isConnected()).thenReturn(true)
        Mockito.`when`(remoteDataSource.getExchangeRate())
            .thenThrow(HttpException(Response.error<ExchangeRateResponse>(404, ResponseBody.create(null, ""))))

        try {
            exchangeRateRepository.getLatestRates().first()
            assert(false)
        } catch (e: HttpException) {
            assert(e.code() == 404)
        }
    }
    @Test
    fun `getLatestRates fetches from local when no update needed`() = runTest {
        val fakeExchangeRate = ExchangeRate(
            SampleData.exchangeRateResponse.rates
        )
        Mockito.`when`(updateChecker.shouldUpdateData()).thenReturn(false)
        Mockito.`when`(localDataSource.getExchangeRate()).thenReturn(flow { emit(fakeExchangeRate) })

        val exchangeRate = exchangeRateRepository.getLatestRates().first()

        assert(exchangeRate == fakeExchangeRate)
    }

    @Test
    fun `getLatestRates fetches from local when network not connected`() = runTest {
        val fakeExchangeRate = ExchangeRate(
            SampleData.exchangeRateResponse.rates
        )
        Mockito.`when`(updateChecker.shouldUpdateData()).thenReturn(true)
        Mockito.`when`(networkMonitor.isConnected()).thenReturn(false)
        Mockito.`when`(localDataSource.getExchangeRate())
            .thenReturn(flow { emit(fakeExchangeRate) })

        val exchangeRate = exchangeRateRepository.getLatestRates().first()

        assert(exchangeRate == fakeExchangeRate)
    }
}

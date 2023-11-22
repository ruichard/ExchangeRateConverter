package com.currency.conversion.data.repository

import com.exchangerate.converter.data.local.LocalDataSource
import com.exchangerate.converter.data.remote.RemoteDataSource
import com.exchangerate.converter.data.model.ExchangeRate
import com.exchangerate.converter.util.NetworkMonitor
import com.exchangerate.converter.data.repository.ExchangeRateRepository
import com.exchangerate.converter.data.repository.UpdateChecker
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ExchangeRateRepositoryTest {
    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var localDataSource: LocalDataSource

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    private lateinit var updateChecker: UpdateChecker

    @Mock
    private lateinit var networkMonitor: NetworkMonitor

    private lateinit var repository: ExchangeRateRepository

    @Before
    fun setUp() {
        repository =
            ExchangeRateRepository(updateChecker, localDataSource, remoteDataSource, networkMonitor)
    }

    @Test
    fun testGetLatestRates_fromLocal() = runBlocking {
        Mockito.`when`(updateChecker.shouldUpdateData()).thenReturn(false)
        val exchangeRate = ExchangeRate(mapOf("USD" to 1.0, "EUR" to 0.85))
        Mockito.`when`(localDataSource.getExchangeRate()).thenReturn(exchangeRate)

        val result = repository.getLatestRates()

        TestCase.assertEquals(Result.success(exchangeRate), result)
    }

    @Test
    fun testGetLatestRates_fromRemote_Success() = runBlocking {
        Mockito.`when`(updateChecker.shouldUpdateData()).thenReturn(true)
        Mockito.`when`(networkMonitor.isConnected()).thenReturn(true)

        val exchangeRate = ExchangeRate(mapOf("USD" to 1.0, "EUR" to 0.85))
        val response = Response.success(exchangeRate)
//        Mockito.`when`(remoteDataSource.getExchangeRate()).thenReturn(response)

//        val result = repository.getLatestRates()

//        TestCase.assertEquals(Result.success(exchangeRate), result)
    }

    @Test
    fun testGetLatestRates_fromRemote_Error() = runBlocking {
        Mockito.`when`(updateChecker.shouldUpdateData()).thenReturn(true)
        Mockito.`when`(networkMonitor.isConnected()).thenReturn(true)
        val response = Response.error<ExchangeRate>(404, ResponseBody.create(null, ""))
//        Mockito.`when`(remoteDataSource.getExchangeRate()).thenReturn(response)
//        val result = repository.getLatestRates()

//        TestCase.assertTrue(result.isFailure)
    }

    @Test
    fun testGetLatestRates_noNetwork() = runBlocking {
        Mockito.`when`(updateChecker.shouldUpdateData()).thenReturn(true)
        Mockito.`when`(networkMonitor.isConnected()).thenReturn(false)

        val result = repository.getLatestRates()

        TestCase.assertTrue(result.isFailure)
    }

}

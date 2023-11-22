package com.exchangerate.converter.di

import com.exchangerate.converter.data.local.ExchangeRateLocalDataSource
import com.exchangerate.converter.data.local.LocalDataSource
import com.exchangerate.converter.data.remote.ExchangeRateRemoteDataSource
import com.exchangerate.converter.data.remote.RemoteDataSource
import com.exchangerate.converter.util.INetworkMonitor
import com.exchangerate.converter.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsExchangeRateLocalDataSource(
        exchangeRateLocalDataSource: ExchangeRateLocalDataSource
    ): LocalDataSource

    @Binds
    fun bindsExchangeRateRemoteDataSource(
        exchangeRateRemoteDataSource: ExchangeRateRemoteDataSource
    ): RemoteDataSource

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: NetworkMonitor,
    ): INetworkMonitor

}
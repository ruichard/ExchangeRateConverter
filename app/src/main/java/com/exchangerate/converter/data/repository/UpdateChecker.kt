package com.exchangerate.converter.data.repository

import androidx.annotation.OpenForTesting
import com.exchangerate.converter.data.local.LocalDataSource
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@OpenForTesting
@Singleton
class UpdateChecker @Inject constructor(
    private val localDataSource: LocalDataSource
) {

    companion object {
        private const val UPDATE_TIME = 30 * 60 * 1000
    }

    suspend fun shouldUpdateData(): Boolean {
        return localDataSource.getLastUpdateTime().firstOrNull()?.let {
            (System.currentTimeMillis() - it) >= UPDATE_TIME
        } ?: true
    }
}

package com.exchangerate.converter.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.OpenForTesting
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@OpenForTesting
@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
) : INetworkMonitor {

    override fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false

        return connectivityManager.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activeNetwork?.let { network ->
                    getNetworkCapabilities(network)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                } ?: false
            } else {
                activeNetworkInfo?.isConnected ?: false
            }
        } ?: false
    }
}

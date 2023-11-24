package com.exchangerate.converter.util

import com.exchangerate.converter.BuildConfig

object Constants {
    const val OPEN_EXCHANGE_API_ID = BuildConfig.OPEN_EXCHANGE_API_ID

    const val MMKV_EXCHANGE_RATE = "mmkv_exchange_rate"
    const val MMKV_LAST_UPDATE_TIME = "mmkv_last_update_time"

    const val OPENEXCHANGERATES_BASE_URL = "https://openexchangerates.org/api/"
}

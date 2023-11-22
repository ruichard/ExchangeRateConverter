package com.exchangerate.converter.util

import android.content.Context
import android.widget.Toast

object ToastUtil {

    fun showLong(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}

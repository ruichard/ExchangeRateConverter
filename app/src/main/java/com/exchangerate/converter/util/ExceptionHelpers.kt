package com.exchangerate.converter.util

fun throwCustomException(message: String, cause: Throwable? = null): Nothing {
    throw Exception(message, cause)
}

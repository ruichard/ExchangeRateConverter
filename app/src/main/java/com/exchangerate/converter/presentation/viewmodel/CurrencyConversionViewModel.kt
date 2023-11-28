package com.exchangerate.converter.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exchangerate.converter.domain.model.Currency
import com.exchangerate.converter.domain.usecase.CalculateConvertedAmountsUseCase
import com.exchangerate.converter.domain.usecase.GetLatestExchangeRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConversionViewModel @Inject constructor(
    private val getLatestExchangeRatesUseCase: GetLatestExchangeRatesUseCase,
    private val calculateConvertedAmountsUseCase: CalculateConvertedAmountsUseCase,
) : ViewModel() {

    private val _currencies = MutableStateFlow<List<String>>(emptyList())
    val currencies = _currencies.asStateFlow()

    private val _exchangeRateState = MutableStateFlow<List<Currency>>(emptyList())
    val exchangeRateState = _exchangeRateState.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    init {
        fetchCurrencies()
    }

    fun fetchCurrencies() {
        viewModelScope.launch {
            getLatestExchangeRatesUseCase()
                .catch {
                    handleFailure(it.message ?: "getLatestExchangeRatesUseCase error")
                }.collect { exchangeRate ->
                    _currencies.value = exchangeRate.rates.keys.toList()
                }
        }
    }

    private fun calculateConvertedAmounts(amount: Double, currencyCode: String) {
        viewModelScope.launch {
            calculateConvertedAmountsUseCase(amount, currencyCode).catch {
                handleFailure(it.message ?: "calculateConvertedAmountsUseCase error")
            }.collect { convertedAmounts ->
                _exchangeRateState.value = convertedAmounts
            }
        }
    }

    fun handleInput(amountText: String, currencyCode: String?) {
        when {
            currencyCode.isNullOrEmpty() -> {
                handleFailure("Currency code is missing.")
            }

            amountText.isEmpty() && _exchangeRateState.value.isNotEmpty() -> {
                _exchangeRateState.value = emptyList()
            }

            else -> {
                val amount = amountText.toDoubleOrNull()
                if (amount == null || amount <= 0) {
                    handleFailure("Invalid input: Amount must be a positive number.")
                } else {
                    calculateConvertedAmounts(amount, currencyCode)
                }
            }
        }
    }

    fun handleFailure(message: String) {
        viewModelScope.launch {
            _error.emit(message)
        }
    }

}

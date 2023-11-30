package com.exchangerate.converter.presentation.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exchangerate.converter.domain.model.Currency
import com.exchangerate.converter.presentation.ui.theme.ExchangeRateConverterTheme
import com.exchangerate.converter.presentation.viewmodel.CurrencyConversionViewModel

@Composable
fun MainScreen(currencyConversionViewModel: CurrencyConversionViewModel = hiltViewModel()) {
    val currencies by currencyConversionViewModel.currencies.collectAsState()
    val exchangeRateState by currencyConversionViewModel.exchangeRateState.collectAsState()
    var selectedCurrency by remember { mutableStateOf("") }
    var enteredAmount by remember { mutableStateOf("") }

    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            AmountInput(
                amount = enteredAmount,
                onAmountChange = { amount ->
                    enteredAmount = amount
                    currencyConversionViewModel.handleInput(enteredAmount, selectedCurrency)
                },
                modifier = Modifier
                    .weight(7f)
            )
            CurrencyDropdown(
                modifier = Modifier
                    .weight(3f)
                    .align(Alignment.Bottom),
                currencies = currencies,
                onCurrencySelected = { currency ->
                    selectedCurrency = currency
                    currencyConversionViewModel.handleInput(enteredAmount, selectedCurrency)
                },
                onInitialCurrencySet = { defaultCurrency ->
                    selectedCurrency = defaultCurrency
                }
            )
        }

        ExchangeRateList(exchangeRateState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropdown(
    modifier: Modifier,
    currencies: List<String>,
    onCurrencySelected: (currency: String) -> Unit,
    onInitialCurrencySet: (currency: String) -> Unit
) {

    var selectedCurrency by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val sizeOfOneItem by remember { mutableStateOf(50.dp) }
    val configuration = LocalConfiguration.current

    val screenHeight50 by remember {
        val screenHeight = configuration.screenHeightDp.dp
        mutableStateOf(screenHeight / 2)
    }

    val height by remember(currencies.size) {
        val itemsSize = sizeOfOneItem * currencies.size
        mutableStateOf(minOf(itemsSize, screenHeight50))
    }

    LaunchedEffect(currencies) {
        if (currencies.isNotEmpty() && selectedCurrency.isEmpty()) {
            selectedCurrency = currencies.first()
            onInitialCurrencySet(selectedCurrency)
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        TextField(
            value = selectedCurrency,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .width(100.dp)
                    .height(height)
            ) {
                items(currencies) { currency ->
                    DropdownMenuItem(
                        onClick = {
                            selectedCurrency = currency
                            expanded = false
                            onCurrencySelected(currency)
                        },
                        text = {
                            Text(text = currency)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountInput(
    modifier: Modifier,
    amount: String,
    onAmountChange: (String) -> Unit
) {
    OutlinedTextField(
        value = amount,
        onValueChange = onAmountChange,
        label = { Text("Input Amount") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier,
    )
}

@Composable
fun ExchangeRateList(currencies: List<Currency>) {
    LazyColumn(modifier = Modifier.padding(start = 4.dp, top = 4.dp)) {
        items(currencies) { currency ->
            Text(
                text = "${currency.currencyCode} : ${currency.amount}",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConversation() {
    ExchangeRateConverterTheme {
        MainScreen()
    }
}
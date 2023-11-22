package com.exchangerate.converter.presentation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.exchangerate.converter.databinding.ActivityCurrencyConversionBinding
import com.exchangerate.converter.presentation.viewmodel.CurrencyConversionViewModel
import com.exchangerate.converter.presentation.adapter.CurrencyConversionListAdapter
import com.exchangerate.converter.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyConversionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCurrencyConversionBinding

    private val viewModel: CurrencyConversionViewModel by viewModels()

    @Inject
    lateinit var spinnerAdapter: ArrayAdapter<String>

    @Inject
    lateinit var ratesAdapter: CurrencyConversionListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrencyConversionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
        setCollect()
    }

    private fun setUI() {
        binding.apply {
            currencyRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@CurrencyConversionActivity)
                adapter = ratesAdapter
            }

            currencySpinner.apply {
                adapter = spinnerAdapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        handleInput()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

            }

            amountInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    handleInput()
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun handleInput() {
        viewModel.handleInput(binding.amountInput.text.toString(), binding.currencySpinner.selectedItem?.toString())
    }

    private fun setCollect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.currencies.collect { currencies ->
                        spinnerAdapter.clear()
                        spinnerAdapter.addAll(currencies)
                        spinnerAdapter.notifyDataSetChanged()
                    }
                }
                launch {
                    viewModel.exchangeRateState.collect { convertedAmounts ->
                        ratesAdapter.submitList(convertedAmounts)
                    }
                }
                launch {
                    viewModel.error.collect { errorMessage ->
                        if (errorMessage.isNotEmpty()) {
                            ToastUtil.showLong(this@CurrencyConversionActivity, errorMessage)
                        }
                    }
                }
            }
        }

    }


}

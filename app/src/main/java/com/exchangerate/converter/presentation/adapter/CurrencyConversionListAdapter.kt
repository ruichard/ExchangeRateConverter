package com.exchangerate.converter.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exchangerate.converter.databinding.ItemConversionBinding
import com.exchangerate.converter.domain.model.Currency
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CurrencyConversionListAdapter @Inject constructor(): ListAdapter<Currency, CurrencyConversionListAdapter.ViewHolder>(
    DIFF_CALLBACK
){

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Currency>() {
            override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
                return oldItem.currencyCode == newItem.currencyCode
            }

            override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConversionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversion = getItem(position)
        holder.bind(conversion)
    }

    class ViewHolder(private val binding: ItemConversionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(conversion: Currency) {
            binding.currencyCodeText.text = conversion.currencyCode
            binding.convertedAmountText.text = conversion.amount.toString()
        }
    }
}


package com.exchangerate.converter.di

import android.content.Context
import android.widget.ArrayAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object AdapterModule {

    @Provides
    fun provideSpinnerAdapter(@ActivityContext context: Context): ArrayAdapter<String> {
        return ArrayAdapter(context, android.R.layout.simple_spinner_item)
    }

}

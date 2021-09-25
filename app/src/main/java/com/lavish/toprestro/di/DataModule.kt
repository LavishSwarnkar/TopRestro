package com.lavish.toprestro.di

import android.content.Context
import com.lavish.toprestro.other.NewPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Singleton
    @Provides
    fun getPrefs(@ApplicationContext context: Context): NewPrefs {
        return NewPrefs(context)
    }

}
package com.lavish.toprestro.di

import android.content.Context
import com.lavish.toprestro.firebaseHelpers.common.LoginHelper
import com.lavish.toprestro.other.NewPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
class AppModule {

    @ActivityScoped
    @Provides
    fun getPrefs(@ActivityContext context: Context): NewPrefs {
        return NewPrefs(context)
    }

    @ActivityScoped
    @Provides
    fun getLoginHelper(@ActivityContext context: Context): LoginHelper {
        return LoginHelper()
    }

}
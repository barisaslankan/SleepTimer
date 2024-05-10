package com.barisaslankan.sleeptimer.di

import android.content.Context
import com.barisaslankan.sleeptimer.presentation.media.MediaManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideMediaManager(
        @ApplicationContext context : Context
    ) : MediaManager{
        return MediaManager(context = context)
    }
}
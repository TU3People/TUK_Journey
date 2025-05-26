package com.example.journey.data.remote.model.di

import com.example.journey.data.remote.api.TimetableApiService
import com.example.journey.data.remote.model.repo.ScheduleRepository
import com.example.journey.data.remote.network.RetrofitProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton @Provides
    fun provideTimetableApi(): TimetableApiService =
        RetrofitProvider.timetableApi

    @Singleton @Provides
    fun provideScheduleRepository(
        api: TimetableApiService
    ): ScheduleRepository = ScheduleRepository(api)
}
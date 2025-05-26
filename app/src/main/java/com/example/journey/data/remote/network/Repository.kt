package com.example.journey.data.remote.network

import com.example.journey.data.remote.api.TimetableApiService
import com.example.journey.data.remote.model.repo.ScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideApi(): TimetableApiService =
        RetrofitProvider.timetableApi         // 기존 싱글톤 재사용
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds abstract fun bindScheduleRepo(
        impl: ScheduleRepository
    ): ScheduleRepository
}
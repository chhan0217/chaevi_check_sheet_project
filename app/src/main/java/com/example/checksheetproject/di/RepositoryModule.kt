package com.example.checksheetproject.di

import com.example.checksheetproject.data.remote.ChargerRemoteDataSource
import com.example.checksheetproject.data.remote.InspectionRemoteDataSource
import com.example.checksheetproject.data.remote.RetrofitChargerRemoteDataSource
import com.example.checksheetproject.data.remote.RetrofitInspectionRemoteDataSource
import com.example.checksheetproject.data.repository.DefaultChargerRepository
import com.example.checksheetproject.data.repository.DefaultInspectionRepository
import com.example.checksheetproject.domain.repository.ChargerRepository
import com.example.checksheetproject.domain.repository.InspectionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindChargerRemoteDataSource(
        retrofitChargerRemoteDataSource: RetrofitChargerRemoteDataSource,
    ): ChargerRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindInspectionRemoteDataSource(
        retrofitInspectionRemoteDataSource: RetrofitInspectionRemoteDataSource,
    ): InspectionRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindChargerRepository(
        defaultChargerRepository: DefaultChargerRepository,
    ): ChargerRepository

    @Binds
    @Singleton
    abstract fun bindInspectionRepository(
        defaultInspectionRepository: DefaultInspectionRepository,
    ): InspectionRepository
}

package com.example.checksheetproject.di

import com.example.checksheetproject.data.remote.ChargerRemoteDataSource
import com.example.checksheetproject.data.remote.RetrofitChargerRemoteDataSource
import com.example.checksheetproject.data.repository.DefaultChargerRepository
import com.example.checksheetproject.domain.repository.ChargerRepository
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
    abstract fun bindChargerRepository(
        defaultChargerRepository: DefaultChargerRepository,
    ): ChargerRepository
}

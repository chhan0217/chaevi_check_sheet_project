package com.example.checksheetproject.di

import com.example.checksheetproject.data.local.AppInspectionDraftDirectoryProvider
import com.example.checksheetproject.data.local.FileInspectionDraftLocalDataSource
import com.example.checksheetproject.data.local.InspectionDraftDirectoryProvider
import com.example.checksheetproject.data.local.InspectionDraftLocalDataSource
import com.example.checksheetproject.data.remote.ChargerRemoteDataSource
import com.example.checksheetproject.data.remote.ConfigurableChargerRemoteDataSource
import com.example.checksheetproject.data.remote.ConfigurableInspectionRemoteDataSource
import com.example.checksheetproject.data.remote.InspectionRemoteDataSource
import com.example.checksheetproject.data.repository.DefaultChargerRepository
import com.example.checksheetproject.data.repository.DefaultInspectionDraftRepository
import com.example.checksheetproject.data.repository.DefaultInspectionRepository
import com.example.checksheetproject.domain.repository.ChargerRepository
import com.example.checksheetproject.domain.repository.InspectionDraftRepository
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
        configurableChargerRemoteDataSource: ConfigurableChargerRemoteDataSource,
    ): ChargerRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindInspectionRemoteDataSource(
        configurableInspectionRemoteDataSource: ConfigurableInspectionRemoteDataSource,
    ): InspectionRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindInspectionDraftLocalDataSource(
        fileInspectionDraftLocalDataSource: FileInspectionDraftLocalDataSource,
    ): InspectionDraftLocalDataSource

    @Binds
    @Singleton
    abstract fun bindInspectionDraftDirectoryProvider(
        appInspectionDraftDirectoryProvider: AppInspectionDraftDirectoryProvider,
    ): InspectionDraftDirectoryProvider

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

    @Binds
    @Singleton
    abstract fun bindInspectionDraftRepository(
        defaultInspectionDraftRepository: DefaultInspectionDraftRepository,
    ): InspectionDraftRepository
}

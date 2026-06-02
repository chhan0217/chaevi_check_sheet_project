package com.example.checksheetproject.di

import com.example.checksheetproject.domain.repository.ChargerRepository
import com.example.checksheetproject.domain.repository.InspectionRepository
import com.example.checksheetproject.domain.usecase.GetFocusedChargersUseCase
import com.example.checksheetproject.domain.usecase.SaveInspectionSubmissionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetFocusedChargersUseCase(
        chargerRepository: ChargerRepository,
    ): GetFocusedChargersUseCase {
        return GetFocusedChargersUseCase(chargerRepository)
    }

    @Provides
    fun provideSaveInspectionSubmissionUseCase(
        inspectionRepository: InspectionRepository,
    ): SaveInspectionSubmissionUseCase {
        return SaveInspectionSubmissionUseCase(inspectionRepository)
    }
}

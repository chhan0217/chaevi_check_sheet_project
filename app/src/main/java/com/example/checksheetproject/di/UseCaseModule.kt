package com.example.checksheetproject.di

import com.example.checksheetproject.domain.repository.ChargerRepository
import com.example.checksheetproject.domain.repository.InspectionDraftRepository
import com.example.checksheetproject.domain.repository.InspectionRepository
import com.example.checksheetproject.domain.usecase.DeleteExpiredInspectionDraftsUseCase
import com.example.checksheetproject.domain.usecase.DeleteInspectionDraftUseCase
import com.example.checksheetproject.domain.usecase.GetChargerUseCase
import com.example.checksheetproject.domain.usecase.GetFocusedChargersUseCase
import com.example.checksheetproject.domain.usecase.GetInspectionDraftUseCase
import com.example.checksheetproject.domain.usecase.SaveInspectionDraftUseCase
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
    fun provideGetChargerUseCase(
        chargerRepository: ChargerRepository,
    ): GetChargerUseCase {
        return GetChargerUseCase(chargerRepository)
    }

    @Provides
    fun provideSaveInspectionSubmissionUseCase(
        inspectionRepository: InspectionRepository,
    ): SaveInspectionSubmissionUseCase {
        return SaveInspectionSubmissionUseCase(inspectionRepository)
    }

    @Provides
    fun provideSaveInspectionDraftUseCase(
        inspectionDraftRepository: InspectionDraftRepository,
    ): SaveInspectionDraftUseCase {
        return SaveInspectionDraftUseCase(inspectionDraftRepository)
    }

    @Provides
    fun provideGetInspectionDraftUseCase(
        inspectionDraftRepository: InspectionDraftRepository,
    ): GetInspectionDraftUseCase {
        return GetInspectionDraftUseCase(inspectionDraftRepository)
    }

    @Provides
    fun provideDeleteInspectionDraftUseCase(
        inspectionDraftRepository: InspectionDraftRepository,
    ): DeleteInspectionDraftUseCase {
        return DeleteInspectionDraftUseCase(inspectionDraftRepository)
    }

    @Provides
    fun provideDeleteExpiredInspectionDraftsUseCase(
        inspectionDraftRepository: InspectionDraftRepository,
    ): DeleteExpiredInspectionDraftsUseCase {
        return DeleteExpiredInspectionDraftsUseCase(inspectionDraftRepository)
    }
}

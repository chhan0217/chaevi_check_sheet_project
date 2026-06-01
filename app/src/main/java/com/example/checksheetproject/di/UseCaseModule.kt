package com.example.checksheetproject.di

import com.example.checksheetproject.domain.repository.ChargerRepository
import com.example.checksheetproject.domain.usecase.GetFocusedChargersUseCase
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
}

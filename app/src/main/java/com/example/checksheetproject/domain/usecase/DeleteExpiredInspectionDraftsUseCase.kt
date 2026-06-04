package com.example.checksheetproject.domain.usecase

import com.example.checksheetproject.domain.repository.InspectionDraftRepository

class DeleteExpiredInspectionDraftsUseCase(
    private val inspectionDraftRepository: InspectionDraftRepository,
) {
    suspend operator fun invoke(nowMillis: Long = System.currentTimeMillis()) {
        inspectionDraftRepository.deleteExpiredDrafts(nowMillis = nowMillis)
    }
}

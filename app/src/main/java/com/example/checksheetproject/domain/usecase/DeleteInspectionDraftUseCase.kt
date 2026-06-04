package com.example.checksheetproject.domain.usecase

import com.example.checksheetproject.domain.repository.InspectionDraftRepository

class DeleteInspectionDraftUseCase(
    private val inspectionDraftRepository: InspectionDraftRepository,
) {
    suspend operator fun invoke(
        chargerId: String,
        inspectionMonth: String,
    ) {
        inspectionDraftRepository.deleteDraft(
            chargerId = chargerId,
            inspectionMonth = inspectionMonth,
        )
    }
}

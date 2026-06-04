package com.example.checksheetproject.domain.usecase

import com.example.checksheetproject.domain.model.InspectionSubmissionPayload
import com.example.checksheetproject.domain.model.hasInspectionContent
import com.example.checksheetproject.domain.repository.InspectionDraftRepository

class SaveInspectionDraftUseCase(
    private val inspectionDraftRepository: InspectionDraftRepository,
) {
    suspend operator fun invoke(payload: InspectionSubmissionPayload) {
        if (!payload.hasInspectionContent) return

        inspectionDraftRepository.saveDraft(payload)
    }
}

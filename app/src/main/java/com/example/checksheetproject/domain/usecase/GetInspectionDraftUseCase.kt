package com.example.checksheetproject.domain.usecase

import com.example.checksheetproject.domain.model.InspectionSubmissionPayload
import com.example.checksheetproject.domain.repository.InspectionDraftRepository

class GetInspectionDraftUseCase(
    private val inspectionDraftRepository: InspectionDraftRepository,
) {
    suspend operator fun invoke(
        chargerId: String,
        inspectionMonth: String,
    ): InspectionSubmissionPayload? {
        return inspectionDraftRepository.getDraft(
            chargerId = chargerId,
            inspectionMonth = inspectionMonth,
        )
    }
}

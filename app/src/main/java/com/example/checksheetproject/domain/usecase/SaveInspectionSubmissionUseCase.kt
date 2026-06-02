package com.example.checksheetproject.domain.usecase

import com.example.checksheetproject.domain.model.InspectionSubmissionPayload
import com.example.checksheetproject.domain.repository.InspectionRepository

class SaveInspectionSubmissionUseCase(
    private val inspectionRepository: InspectionRepository,
) {
    suspend operator fun invoke(payload: InspectionSubmissionPayload) {
        inspectionRepository.saveInspection(payload)
    }
}

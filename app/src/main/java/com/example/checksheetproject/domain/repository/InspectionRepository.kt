package com.example.checksheetproject.domain.repository

import com.example.checksheetproject.domain.model.InspectionSubmissionPayload

interface InspectionRepository {
    suspend fun saveInspection(payload: InspectionSubmissionPayload)
}

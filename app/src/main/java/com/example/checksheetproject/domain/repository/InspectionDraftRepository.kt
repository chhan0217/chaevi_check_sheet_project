package com.example.checksheetproject.domain.repository

import com.example.checksheetproject.domain.model.InspectionSubmissionPayload

interface InspectionDraftRepository {
    suspend fun getDraft(
        chargerId: String,
        inspectionMonth: String,
    ): InspectionSubmissionPayload?

    suspend fun saveDraft(payload: InspectionSubmissionPayload)

    suspend fun deleteDraft(
        chargerId: String,
        inspectionMonth: String,
    )

    suspend fun deleteExpiredDrafts(nowMillis: Long)
}

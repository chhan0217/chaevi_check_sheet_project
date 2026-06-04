package com.example.checksheetproject.data.local

import com.example.checksheetproject.data.remote.dto.InspectionSubmissionRequest

interface InspectionDraftLocalDataSource {
    suspend fun getDraft(
        chargerId: String,
        inspectionMonth: String,
    ): InspectionSubmissionRequest?

    suspend fun saveDraft(request: InspectionSubmissionRequest)

    suspend fun deleteDraft(
        chargerId: String,
        inspectionMonth: String,
    )

    suspend fun deleteExpiredDrafts(nowMillis: Long)
}

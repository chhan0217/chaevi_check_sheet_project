package com.example.checksheetproject.data.local

interface InspectionDraftLocalDataSource {
    suspend fun getDraft(
        chargerId: String,
        inspectionMonth: String,
    ): InspectionDraftEntity?

    suspend fun saveDraft(entity: InspectionDraftEntity)

    suspend fun deleteDraft(
        chargerId: String,
        inspectionMonth: String,
    )

    suspend fun deleteExpiredDrafts(nowMillis: Long)
}

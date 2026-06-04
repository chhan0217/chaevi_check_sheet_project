package com.example.checksheetproject.data.repository

import com.example.checksheetproject.data.local.InspectionDraftLocalDataSource
import com.example.checksheetproject.data.mapper.toDomain
import com.example.checksheetproject.data.mapper.toRequest
import com.example.checksheetproject.domain.model.InspectionSubmissionPayload
import com.example.checksheetproject.domain.repository.InspectionDraftRepository
import javax.inject.Inject

class DefaultInspectionDraftRepository @Inject constructor(
    private val inspectionDraftLocalDataSource: InspectionDraftLocalDataSource,
) : InspectionDraftRepository {
    override suspend fun getDraft(
        chargerId: String,
        inspectionMonth: String,
    ): InspectionSubmissionPayload? {
        return inspectionDraftLocalDataSource.getDraft(
            chargerId = chargerId,
            inspectionMonth = inspectionMonth,
        )?.toDomain()
    }

    override suspend fun saveDraft(payload: InspectionSubmissionPayload) {
        inspectionDraftLocalDataSource.saveDraft(payload.toRequest())
    }

    override suspend fun deleteDraft(
        chargerId: String,
        inspectionMonth: String,
    ) {
        inspectionDraftLocalDataSource.deleteDraft(
            chargerId = chargerId,
            inspectionMonth = inspectionMonth,
        )
    }

    override suspend fun deleteExpiredDrafts(nowMillis: Long) {
        inspectionDraftLocalDataSource.deleteExpiredDrafts(nowMillis = nowMillis)
    }
}

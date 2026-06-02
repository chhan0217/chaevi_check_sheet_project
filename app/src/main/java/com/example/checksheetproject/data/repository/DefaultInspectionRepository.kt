package com.example.checksheetproject.data.repository

import com.example.checksheetproject.data.mapper.toRequest
import com.example.checksheetproject.data.remote.InspectionRemoteDataSource
import com.example.checksheetproject.domain.model.InspectionSubmissionPayload
import com.example.checksheetproject.domain.repository.InspectionRepository
import javax.inject.Inject

class DefaultInspectionRepository @Inject constructor(
    private val inspectionRemoteDataSource: InspectionRemoteDataSource,
) : InspectionRepository {
    override suspend fun saveInspection(payload: InspectionSubmissionPayload) {
        inspectionRemoteDataSource.saveInspection(payload.toRequest())
    }
}

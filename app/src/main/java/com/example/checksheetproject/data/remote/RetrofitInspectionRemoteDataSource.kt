package com.example.checksheetproject.data.remote

import com.example.checksheetproject.data.remote.dto.InspectionSubmissionRequest
import javax.inject.Inject

class RetrofitInspectionRemoteDataSource @Inject constructor(
    private val checkSheetApi: CheckSheetApi,
) : InspectionRemoteDataSource {
    override suspend fun saveInspection(request: InspectionSubmissionRequest) {
        checkSheetApi.saveInspection(request)
    }
}

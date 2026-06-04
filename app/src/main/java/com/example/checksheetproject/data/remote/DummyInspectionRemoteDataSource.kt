package com.example.checksheetproject.data.remote

import com.example.checksheetproject.data.remote.dto.InspectionSubmissionRequest
import javax.inject.Inject

class DummyInspectionRemoteDataSource @Inject constructor() : InspectionRemoteDataSource {
    var lastSavedRequest: InspectionSubmissionRequest? = null
        private set

    override suspend fun saveInspection(request: InspectionSubmissionRequest) {
        lastSavedRequest = request
    }
}

package com.example.checksheetproject.data.remote

import com.example.checksheetproject.data.remote.dto.InspectionSubmissionRequest

interface InspectionRemoteDataSource {
    suspend fun saveInspection(request: InspectionSubmissionRequest)
}

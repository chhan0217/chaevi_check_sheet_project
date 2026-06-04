package com.example.checksheetproject.data.remote

import com.example.checksheetproject.data.remote.dto.InspectionSubmissionRequest
import javax.inject.Inject

class ConfigurableInspectionRemoteDataSource @Inject constructor(
    private val retrofitInspectionRemoteDataSource: RetrofitInspectionRemoteDataSource,
    private val dummyInspectionRemoteDataSource: DummyInspectionRemoteDataSource,
) : InspectionRemoteDataSource {
    override suspend fun saveInspection(request: InspectionSubmissionRequest) {
        if (CheckSheetNetworkConfig.USE_DUMMY_DATA) {
            dummyInspectionRemoteDataSource.saveInspection(request)
        } else {
            retrofitInspectionRemoteDataSource.saveInspection(request)
        }
    }
}

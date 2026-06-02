package com.example.checksheetproject.data.remote

import com.example.checksheetproject.data.remote.dto.ChargerResponse
import com.example.checksheetproject.data.remote.dto.InspectionSubmissionRequest
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckSheetApi {
    @GET("chargers")
    suspend fun getChargers(): List<ChargerResponse>

    @POST("inspections")
    suspend fun saveInspection(
        @Body request: InspectionSubmissionRequest,
    )
}

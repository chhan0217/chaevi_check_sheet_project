package com.example.checksheetproject.data.remote

import com.example.checksheetproject.data.remote.dto.ChargerResponse
import retrofit2.http.GET

interface CheckSheetApi {
    @GET("chargers")
    suspend fun getChargers(): List<ChargerResponse>
}

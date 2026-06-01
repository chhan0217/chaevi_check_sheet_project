package com.example.checksheetproject.data.remote

import com.example.checksheetproject.data.remote.dto.ChargerResponse

interface ChargerRemoteDataSource {
    suspend fun getChargers(): List<ChargerResponse>
}

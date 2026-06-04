package com.example.checksheetproject.data.remote

import com.example.checksheetproject.data.remote.dto.ChargerResponse
import javax.inject.Inject

class DummyChargerRemoteDataSource @Inject constructor() : ChargerRemoteDataSource {
    override suspend fun getChargers(): List<ChargerResponse> {
        return DummyCheckSheetData.chargers
    }
}

package com.example.checksheetproject.data.remote

import com.example.checksheetproject.data.remote.dto.ChargerResponse
import javax.inject.Inject

class ConfigurableChargerRemoteDataSource @Inject constructor(
    private val retrofitChargerRemoteDataSource: RetrofitChargerRemoteDataSource,
    private val dummyChargerRemoteDataSource: DummyChargerRemoteDataSource,
) : ChargerRemoteDataSource {
    override suspend fun getChargers(): List<ChargerResponse> {
        return if (CheckSheetNetworkConfig.USE_DUMMY_DATA) {
            dummyChargerRemoteDataSource.getChargers()
        } else {
            retrofitChargerRemoteDataSource.getChargers()
        }
    }
}

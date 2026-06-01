package com.example.checksheetproject.data.repository

import com.example.checksheetproject.data.mapper.toDomain
import com.example.checksheetproject.data.remote.ChargerRemoteDataSource
import com.example.checksheetproject.domain.model.Charger
import com.example.checksheetproject.domain.repository.ChargerRepository
import javax.inject.Inject

class DefaultChargerRepository @Inject constructor(
    private val chargerRemoteDataSource: ChargerRemoteDataSource,
) : ChargerRepository {
    override suspend fun getChargers(): List<Charger> {
        return chargerRemoteDataSource.getChargers().map { it.toDomain() }
    }
}

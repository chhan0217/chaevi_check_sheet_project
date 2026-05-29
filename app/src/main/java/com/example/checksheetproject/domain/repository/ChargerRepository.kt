package com.example.checksheetproject.domain.repository

import com.example.checksheetproject.domain.model.Charger

interface ChargerRepository {
    suspend fun getChargers(): List<Charger>
}

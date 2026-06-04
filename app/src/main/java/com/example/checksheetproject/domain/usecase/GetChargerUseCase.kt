package com.example.checksheetproject.domain.usecase

import com.example.checksheetproject.domain.model.Charger
import com.example.checksheetproject.domain.repository.ChargerRepository

class GetChargerUseCase(
    private val chargerRepository: ChargerRepository,
) {
    suspend operator fun invoke(chargerId: String): Charger? {
        return chargerRepository.getChargers()
            .firstOrNull { it.id == chargerId }
    }
}

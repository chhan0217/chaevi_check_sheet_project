package com.example.checksheetproject.domain.usecase

import com.example.checksheetproject.domain.model.Charger
import com.example.checksheetproject.domain.repository.ChargerRepository

class GetFocusedChargersUseCase(
    private val chargerRepository: ChargerRepository,
) {
    suspend operator fun invoke(): List<Charger> {
        return chargerRepository.getChargers()
            .filter { it.isFocusedManagementTarget }
            .sortedBy { it.monthlySalesRankPercent }
    }
}

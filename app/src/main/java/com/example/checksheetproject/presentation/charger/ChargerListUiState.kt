package com.example.checksheetproject.presentation.charger

import com.example.checksheetproject.presentation.common.InspectionStatusUi

data class ChargerListUiState(
    val inspectionMonthLabel: String = "",
    val chargers: List<ChargerItemUi> = emptyList(),
)

data class ChargerItemUi(
    val id: String,
    val name: String,
    val location: String,
    val monthlySalesRank: String,
    val status: InspectionStatusUi,
)

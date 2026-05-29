package com.example.checksheetproject.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {
    @Serializable
    data object Main : AppRoute

    @Serializable
    data object ChargerList : AppRoute

    @Serializable
    data object InspectionChargerEntry : AppRoute

    @Serializable
    data class InspectionItemList(
        val chargerId: String,
    ) : AppRoute

    @Serializable
    data object CleaningGuide : AppRoute

    @Serializable
    data object Settings : AppRoute

    @Serializable
    data class Checklist(
        val chargerId: String,
        val inspectionMonth: String,
    ) : AppRoute
}

package com.example.checksheetproject.presentation.inspectionitem

data class InspectionChargerEntryUiState(
    val chargerIdInput: String = "",
    val isChecking: Boolean = false,
    val isStartingInspection: Boolean = false,
    val chargerInfo: InspectionChargerInfoUi? = null,
    val showDraftLoadDialog: Boolean = false,
    val errorMessage: String? = null,
)

val InspectionChargerEntryUiState.canCheckCharger: Boolean
    get() = chargerIdInput.trim().isNotEmpty() && !isChecking

data class InspectionChargerInfoUi(
    val id: String,
    val name: String,
    val location: String,
    val hasDraft: Boolean = false,
)

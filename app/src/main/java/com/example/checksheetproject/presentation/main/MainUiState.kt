package com.example.checksheetproject.presentation.main

data class MainUiState(
    val title: String = "정기점검 관리",
    val inspectionMonthLabel: String = "2026년 5월",
    val menuItems: List<MainMenuItemUi> = emptyList(),
)

data class MainMenuItemUi(
    val type: MainMenuType,
    val title: String,
    val description: String,
)

enum class MainMenuType {
    FocusedChargers,
    InspectionItems,
    CleaningGuide,
    Settings,
}

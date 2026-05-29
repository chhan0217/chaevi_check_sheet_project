package com.example.checksheetproject.presentation.cleaning

data class CleaningGuideUiState(
    val title: String = "충전기 청소 및 지속관리 방법",
    val sections: List<CleaningGuideSectionUi> = emptyList(),
)

data class CleaningGuideSectionUi(
    val title: String,
    val description: String,
    val tableRows: List<CleaningGuideTableRowUi> = emptyList(),
    val images: List<CleaningGuideImageUi> = emptyList(),
)

data class CleaningGuideTableRowUi(
    val category: String,
    val managementStandard: String,
    val cleaningTool: String,
    val caution: String = "",
)

data class CleaningGuideImageUi(
    val resourceName: String,
    val title: String,
)

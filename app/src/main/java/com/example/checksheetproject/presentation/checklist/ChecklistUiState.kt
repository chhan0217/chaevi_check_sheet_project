package com.example.checksheetproject.presentation.checklist

data class ChecklistUiState(
    val chargerId: String = "",
    val inspectionMonth: String = "",
    val guideMessage: String = "충전기 상태를 항목별로 확인하고 이상 여부를 기록합니다.",
    val items: List<ChecklistItemUi> = emptyList(),
    val memo: String = "",
)

data class ChecklistItemUi(
    val id: String,
    val title: String,
    val selectedStatus: CheckStatus,
)

enum class CheckStatus(val label: String) {
    Normal("정상"),
    Warning("이상"),
    NotApplicable("해당 없음"),
}

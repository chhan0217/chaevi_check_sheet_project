package com.example.checksheetproject.presentation.inspectionitem

data class InspectionItemListUiState(
    val title: String = "점검항목 리스트",
    val groups: List<InspectionItemGroupUi> = emptyList(),
    val currentGroupIndex: Int = 0,
    val canMovePrevious: Boolean = false,
    val canMoveNext: Boolean = false,
    val itemStatuses: Map<String, InspectionCheckStatus> = emptyMap(),
    val measurementValues: Map<String, String> = emptyMap(),
    val issueMemos: Map<String, String> = emptyMap(),
)

val InspectionItemListUiState.currentGroup: InspectionItemGroupUi?
    get() = groups.getOrNull(currentGroupIndex)

val InspectionItemListUiState.isFirstGroup: Boolean
    get() = currentGroupIndex == 0

val InspectionItemListUiState.isLastGroup: Boolean
    get() = groups.isNotEmpty() && currentGroupIndex == groups.lastIndex

val InspectionItemListUiState.isCurrentGroupCompleted: Boolean
    get() {
        val group = currentGroup ?: return false
        return if (group.requiresMeasurementInput) {
            group.items.all { measurementValues[it].orEmpty().isNotBlank() }
        } else {
            group.items.all { itemStatuses.containsKey(it) }
        }
    }

val InspectionItemGroupUi.requiresMeasurementInput: Boolean
    get() = category == "성능 및 저항 확인"

data class InspectionItemGroupUi(
    val category: String,
    val title: String,
    val items: List<String>,
)

enum class InspectionCheckStatus(
    val label: String,
) {
    Normal("정상"),
    Issue("이상"),
    NotApplicable("해당 없음"),
}

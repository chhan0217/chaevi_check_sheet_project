package com.example.checksheetproject.domain.model

data class InspectionSubmissionPayload(
    val chargerId: String = "",
    val inspectionMonth: String = "",
    val inspectorId: String = "",
    val createdAtMillis: Long,
    val createdAtDateTime: String,
    val groups: List<InspectionSubmissionGroup>,
)

data class InspectionSubmissionGroup(
    val category: String,
    val title: String,
    val items: List<InspectionSubmissionItem>,
)

data class InspectionSubmissionItem(
    val itemId: String,
    val title: String,
    val rawText: String,
    val status: InspectionSubmissionStatus,
    val measurementValue: String? = null,
    val issueMemo: String? = null,
)

enum class InspectionSubmissionStatus {
    NORMAL,
    ISSUE,
    NOT_APPLICABLE,
    NOT_SELECTED,
}

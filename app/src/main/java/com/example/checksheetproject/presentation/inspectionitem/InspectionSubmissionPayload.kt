package com.example.checksheetproject.presentation.inspectionitem

import kotlinx.serialization.Serializable

@Serializable
data class InspectionSubmissionPayload(
    val chargerId: String = "",
    val inspectionMonth: String = "",
    val inspectorId: String = "",
    val createdAtMillis: Long,
    val createdAtDateTime: String,
    val groups: List<InspectionSubmissionGroup>,
)

@Serializable
data class InspectionSubmissionGroup(
    val category: String,
    val title: String,
    val items: List<InspectionSubmissionItem>,
)

@Serializable
data class InspectionSubmissionItem(
    val itemId: String,
    val title: String,
    val rawText: String,
    val status: InspectionSubmissionStatus,
    val measurementValue: String? = null,
    val issueMemo: String? = null,
)

@Serializable
enum class InspectionSubmissionStatus {
    NORMAL,
    ISSUE,
    NOT_APPLICABLE,
    NOT_SELECTED,
}

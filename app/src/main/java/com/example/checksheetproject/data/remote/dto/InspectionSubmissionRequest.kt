package com.example.checksheetproject.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class InspectionSubmissionRequest(
    val chargerId: String = "",
    val inspectionMonth: String = "",
    val inspectorId: String = "",
    val createdAtMillis: Long,
    val createdAtDateTime: String,
    val groups: List<InspectionSubmissionGroupRequest>,
)

@Serializable
data class InspectionSubmissionGroupRequest(
    val category: String,
    val title: String,
    val items: List<InspectionSubmissionItemRequest>,
)

@Serializable
data class InspectionSubmissionItemRequest(
    val itemId: String,
    val title: String,
    val rawText: String,
    val status: InspectionSubmissionStatusRequest,
    val measurementValue: String? = null,
    val issueMemo: String? = null,
)

@Serializable
enum class InspectionSubmissionStatusRequest {
    NORMAL,
    ISSUE,
    NOT_APPLICABLE,
    NOT_SELECTED,
}

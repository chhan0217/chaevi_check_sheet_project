package com.example.checksheetproject.data.local

import kotlinx.serialization.Serializable

@Serializable
data class InspectionDraftEntity(
    val chargerId: String = "",
    val inspectionMonth: String = "",
    val inspectorId: String = "",
    val createdAtMillis: Long,
    val createdAtDateTime: String,
    val groups: List<InspectionDraftGroupEntity>,
)

@Serializable
data class InspectionDraftGroupEntity(
    val category: String,
    val title: String,
    val items: List<InspectionDraftItemEntity>,
)

@Serializable
data class InspectionDraftItemEntity(
    val itemId: String,
    val title: String,
    val rawText: String,
    val status: InspectionDraftStatusEntity,
    val measurementValue: String? = null,
    val issueMemo: String? = null,
)

@Serializable
enum class InspectionDraftStatusEntity {
    NORMAL,
    ISSUE,
    NOT_APPLICABLE,
    NOT_SELECTED,
}

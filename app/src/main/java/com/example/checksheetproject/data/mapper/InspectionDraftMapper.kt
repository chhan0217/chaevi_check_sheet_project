package com.example.checksheetproject.data.mapper

import com.example.checksheetproject.data.local.InspectionDraftEntity
import com.example.checksheetproject.data.local.InspectionDraftGroupEntity
import com.example.checksheetproject.data.local.InspectionDraftItemEntity
import com.example.checksheetproject.data.local.InspectionDraftStatusEntity
import com.example.checksheetproject.domain.model.InspectionSubmissionGroup
import com.example.checksheetproject.domain.model.InspectionSubmissionItem
import com.example.checksheetproject.domain.model.InspectionSubmissionPayload
import com.example.checksheetproject.domain.model.InspectionSubmissionStatus

fun InspectionSubmissionPayload.toDraftEntity(): InspectionDraftEntity {
    return InspectionDraftEntity(
        chargerId = chargerId,
        inspectionMonth = inspectionMonth,
        inspectorId = inspectorId,
        createdAtMillis = createdAtMillis,
        createdAtDateTime = createdAtDateTime,
        groups = groups.map { it.toDraftEntity() },
    )
}

private fun InspectionSubmissionGroup.toDraftEntity(): InspectionDraftGroupEntity {
    return InspectionDraftGroupEntity(
        category = category,
        title = title,
        items = items.map { it.toDraftEntity() },
    )
}

private fun InspectionSubmissionItem.toDraftEntity(): InspectionDraftItemEntity {
    return InspectionDraftItemEntity(
        itemId = itemId,
        title = title,
        rawText = rawText,
        status = status.toDraftEntity(),
        measurementValue = measurementValue,
        issueMemo = issueMemo,
    )
}

fun InspectionDraftEntity.toDomain(): InspectionSubmissionPayload {
    return InspectionSubmissionPayload(
        chargerId = chargerId,
        inspectionMonth = inspectionMonth,
        inspectorId = inspectorId,
        createdAtMillis = createdAtMillis,
        createdAtDateTime = createdAtDateTime,
        groups = groups.map { it.toDomain() },
    )
}

private fun InspectionDraftGroupEntity.toDomain(): InspectionSubmissionGroup {
    return InspectionSubmissionGroup(
        category = category,
        title = title,
        items = items.map { it.toDomain() },
    )
}

private fun InspectionDraftItemEntity.toDomain(): InspectionSubmissionItem {
    return InspectionSubmissionItem(
        itemId = itemId,
        title = title,
        rawText = rawText,
        status = status.toDomain(),
        measurementValue = measurementValue,
        issueMemo = issueMemo,
    )
}

private fun InspectionSubmissionStatus.toDraftEntity(): InspectionDraftStatusEntity {
    return when (this) {
        InspectionSubmissionStatus.NORMAL -> InspectionDraftStatusEntity.NORMAL
        InspectionSubmissionStatus.ISSUE -> InspectionDraftStatusEntity.ISSUE
        InspectionSubmissionStatus.NOT_APPLICABLE -> InspectionDraftStatusEntity.NOT_APPLICABLE
        InspectionSubmissionStatus.NOT_SELECTED -> InspectionDraftStatusEntity.NOT_SELECTED
    }
}

private fun InspectionDraftStatusEntity.toDomain(): InspectionSubmissionStatus {
    return when (this) {
        InspectionDraftStatusEntity.NORMAL -> InspectionSubmissionStatus.NORMAL
        InspectionDraftStatusEntity.ISSUE -> InspectionSubmissionStatus.ISSUE
        InspectionDraftStatusEntity.NOT_APPLICABLE -> InspectionSubmissionStatus.NOT_APPLICABLE
        InspectionDraftStatusEntity.NOT_SELECTED -> InspectionSubmissionStatus.NOT_SELECTED
    }
}

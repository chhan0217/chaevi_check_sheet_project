package com.example.checksheetproject.data.mapper

import com.example.checksheetproject.data.remote.dto.InspectionSubmissionGroupRequest
import com.example.checksheetproject.data.remote.dto.InspectionSubmissionItemRequest
import com.example.checksheetproject.data.remote.dto.InspectionSubmissionRequest
import com.example.checksheetproject.data.remote.dto.InspectionSubmissionStatusRequest
import com.example.checksheetproject.domain.model.InspectionSubmissionGroup
import com.example.checksheetproject.domain.model.InspectionSubmissionItem
import com.example.checksheetproject.domain.model.InspectionSubmissionPayload
import com.example.checksheetproject.domain.model.InspectionSubmissionStatus

fun InspectionSubmissionPayload.toRequest(): InspectionSubmissionRequest {
    return InspectionSubmissionRequest(
        chargerId = chargerId,
        inspectionMonth = inspectionMonth,
        inspectorId = inspectorId,
        createdAtMillis = createdAtMillis,
        createdAtDateTime = createdAtDateTime,
        groups = groups.map { it.toRequest() },
    )
}

private fun InspectionSubmissionGroup.toRequest(): InspectionSubmissionGroupRequest {
    return InspectionSubmissionGroupRequest(
        category = category,
        title = title,
        items = items.map { it.toRequest() },
    )
}

private fun InspectionSubmissionItem.toRequest(): InspectionSubmissionItemRequest {
    return InspectionSubmissionItemRequest(
        itemId = itemId,
        title = title,
        rawText = rawText,
        status = status.toRequest(),
        measurementValue = measurementValue,
        issueMemo = issueMemo,
    )
}

private fun InspectionSubmissionStatus.toRequest(): InspectionSubmissionStatusRequest {
    return when (this) {
        InspectionSubmissionStatus.NORMAL -> InspectionSubmissionStatusRequest.NORMAL
        InspectionSubmissionStatus.ISSUE -> InspectionSubmissionStatusRequest.ISSUE
        InspectionSubmissionStatus.NOT_APPLICABLE -> InspectionSubmissionStatusRequest.NOT_APPLICABLE
        InspectionSubmissionStatus.NOT_SELECTED -> InspectionSubmissionStatusRequest.NOT_SELECTED
    }
}

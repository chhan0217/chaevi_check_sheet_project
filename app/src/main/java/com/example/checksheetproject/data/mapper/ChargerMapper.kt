package com.example.checksheetproject.data.mapper

import com.example.checksheetproject.data.remote.dto.ChargerResponse
import com.example.checksheetproject.domain.model.Charger
import com.example.checksheetproject.domain.model.InspectionStatus

fun ChargerResponse.toDomain(): Charger {
    return Charger(
        id = id,
        name = name,
        location = location,
        monthlySalesRankPercent = monthlySalesRankPercent,
        inspectionStatus = inspectionStatus.toInspectionStatus(),
    )
}

private fun String.toInspectionStatus(): InspectionStatus {
    return when (trim().uppercase()) {
        "NOT_INSPECTED", "NOTINSPECTED", "PENDING" -> InspectionStatus.NotInspected
        "COMPLETED", "DONE" -> InspectionStatus.Completed
        "HAS_ISSUE", "HASISSUE", "WARNING", "ISSUE" -> InspectionStatus.HasIssue
        else -> error("Unsupported inspection status: $this")
    }
}

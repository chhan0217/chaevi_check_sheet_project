package com.example.checksheetproject.domain.model

data class Charger(
    val id: String,
    val name: String,
    val location: String,
    val monthlySalesRankPercent: Int,
    val inspectionStatus: InspectionStatus,
) {
    init {
        require(id.isNotBlank()) { "Charger id must not be blank." }
        require(name.isNotBlank()) { "Charger name must not be blank." }
        require(monthlySalesRankPercent in 1..100) {
            "Monthly sales rank percent must be between 1 and 100."
        }
    }

    val isFocusedManagementTarget: Boolean
        get() = monthlySalesRankPercent <= FOCUSED_MANAGEMENT_THRESHOLD_PERCENT

    companion object {
        const val FOCUSED_MANAGEMENT_THRESHOLD_PERCENT = 45
    }
}

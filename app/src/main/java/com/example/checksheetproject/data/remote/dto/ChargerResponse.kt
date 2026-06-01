package com.example.checksheetproject.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChargerResponse(
    val id: String,
    val name: String,
    val location: String,
    val monthlySalesRankPercent: Int,
    val inspectionStatus: String,
)

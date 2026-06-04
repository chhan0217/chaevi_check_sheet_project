package com.example.checksheetproject.data.remote

import com.example.checksheetproject.data.remote.dto.ChargerResponse

object DummyCheckSheetData {
    val chargers = listOf(
        ChargerResponse(
            id = "CHB-001",
            name = "강남센터 급속 01",
            location = "서울 강남구",
            monthlySalesRankPercent = 3,
            inspectionStatus = "NOT_INSPECTED",
        ),
        ChargerResponse(
            id = "CHB-014",
            name = "판교허브 급속 02",
            location = "경기 성남시",
            monthlySalesRankPercent = 12,
            inspectionStatus = "HAS_ISSUE",
        ),
        ChargerResponse(
            id = "CHB-027",
            name = "수원영업소 완속 01",
            location = "경기 수원시",
            monthlySalesRankPercent = 31,
            inspectionStatus = "COMPLETED",
        ),
        ChargerResponse(
            id = "CHB-045",
            name = "부산물류센터 급속 03",
            location = "부산 강서구",
            monthlySalesRankPercent = 45,
            inspectionStatus = "PENDING",
        ),
        ChargerResponse(
            id = "CHB-083",
            name = "대전영업소 급속 01",
            location = "대전 유성구",
            monthlySalesRankPercent = 62,
            inspectionStatus = "DONE",
        ),
    )
}

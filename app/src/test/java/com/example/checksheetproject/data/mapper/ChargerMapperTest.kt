package com.example.checksheetproject.data.mapper

import com.example.checksheetproject.data.remote.dto.ChargerResponse
import com.example.checksheetproject.domain.model.InspectionStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ChargerMapperTest {
    @Test
    fun `서버 충전기 응답을 도메인 모델로 변환한다`() {
        val charger = chargerResponse().toDomain()

        assertEquals("CHB-001", charger.id)
        assertEquals("강남센터 급속 01", charger.name)
        assertEquals("서울 강남구", charger.location)
        assertEquals(3, charger.monthlySalesRankPercent)
        assertEquals(InspectionStatus.NotInspected, charger.inspectionStatus)
    }

    @Test
    fun `지원하지 않는 점검 상태는 변환하지 않는다`() {
        assertThrows(IllegalStateException::class.java) {
            chargerResponse(inspectionStatus = "UNKNOWN").toDomain()
        }
    }

    private fun chargerResponse(
        inspectionStatus: String = "NOT_INSPECTED",
    ): ChargerResponse {
        return ChargerResponse(
            id = "CHB-001",
            name = "강남센터 급속 01",
            location = "서울 강남구",
            monthlySalesRankPercent = 3,
            inspectionStatus = inspectionStatus,
        )
    }
}

package com.example.checksheetproject.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class ChargerTest {
    @Test
    fun `상위 매출 45퍼센트 이내 충전기는 집중관리 대상이다`() {
        val charger = charger(monthlySalesRankPercent = 45)

        assertTrue(charger.isFocusedManagementTarget)
    }

    @Test
    fun `상위 매출 45퍼센트를 초과한 충전기는 집중관리 대상이 아니다`() {
        val charger = charger(monthlySalesRankPercent = 46)

        assertFalse(charger.isFocusedManagementTarget)
    }

    @Test
    fun `매출 순위 비율은 1부터 100까지만 허용한다`() {
        assertThrows(IllegalArgumentException::class.java) {
            charger(monthlySalesRankPercent = 0)
        }

        assertThrows(IllegalArgumentException::class.java) {
            charger(monthlySalesRankPercent = 101)
        }
    }

    private fun charger(
        monthlySalesRankPercent: Int,
    ): Charger {
        return Charger(
            id = "CHB-001",
            name = "강남센터 급속 01",
            location = "서울 강남구",
            monthlySalesRankPercent = monthlySalesRankPercent,
            inspectionStatus = InspectionStatus.NotInspected,
        )
    }
}

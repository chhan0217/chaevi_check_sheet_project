package com.example.checksheetproject.domain.usecase

import com.example.checksheetproject.domain.model.Charger
import com.example.checksheetproject.domain.model.InspectionStatus
import com.example.checksheetproject.domain.repository.ChargerRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class GetFocusedChargersUseCaseTest {
    @Test
    fun `집중관리 대상 충전기만 매출 순위 기준으로 반환한다`() = runTest {
        val useCase = GetFocusedChargersUseCase(
            chargerRepository = FakeChargerRepository(
                chargers = listOf(
                    charger(id = "CHB-003", monthlySalesRankPercent = 46),
                    charger(id = "CHB-001", monthlySalesRankPercent = 12),
                    charger(id = "CHB-002", monthlySalesRankPercent = 3),
                    charger(id = "CHB-004", monthlySalesRankPercent = 45),
                ),
            ),
        )

        val result = useCase()

        assertEquals(
            listOf("CHB-002", "CHB-001", "CHB-004"),
            result.map { it.id },
        )
    }

    @Test
    fun `충전기 조회 실패는 호출자에게 전달한다`() = runTest {
        val useCase = GetFocusedChargersUseCase(
            chargerRepository = FailingChargerRepository,
        )

        try {
            useCase()
            fail("Expected IllegalStateException.")
        } catch (exception: IllegalStateException) {
            assertEquals("Failed to load chargers.", exception.message)
        }
    }

    private fun charger(
        id: String,
        monthlySalesRankPercent: Int,
    ): Charger {
        return Charger(
            id = id,
            name = "충전기 $id",
            location = "서울",
            monthlySalesRankPercent = monthlySalesRankPercent,
            inspectionStatus = InspectionStatus.NotInspected,
        )
    }

    private class FakeChargerRepository(
        private val chargers: List<Charger>,
    ) : ChargerRepository {
        override suspend fun getChargers(): List<Charger> = chargers
    }

    private object FailingChargerRepository : ChargerRepository {
        override suspend fun getChargers(): List<Charger> {
            error("Failed to load chargers.")
        }
    }
}

package com.example.checksheetproject.domain.usecase

import com.example.checksheetproject.domain.model.InspectionSubmissionPayload
import com.example.checksheetproject.domain.repository.InspectionRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SaveInspectionSubmissionUseCaseTest {
    @Test
    fun `점검 저장 payload를 Repository에 전달한다`() = runTest {
        val repository = FakeInspectionRepository()
        val useCase = SaveInspectionSubmissionUseCase(repository)
        val payload = InspectionSubmissionPayload(
            chargerId = "CHARGER-001",
            inspectionMonth = "2026-06",
            inspectorId = "INSPECTOR-001",
            createdAtMillis = 1000L,
            createdAtDateTime = "2026-06-02 10:00:00",
            groups = emptyList(),
        )

        useCase(payload)

        assertEquals(payload, repository.savedPayload)
    }

    private class FakeInspectionRepository : InspectionRepository {
        var savedPayload: InspectionSubmissionPayload? = null
            private set

        override suspend fun saveInspection(payload: InspectionSubmissionPayload) {
            savedPayload = payload
        }
    }
}

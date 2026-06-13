package com.example.checksheetproject.domain.usecase

import com.example.checksheetproject.domain.model.InspectionSubmissionGroup
import com.example.checksheetproject.domain.model.InspectionSubmissionItem
import com.example.checksheetproject.domain.model.InspectionSubmissionPayload
import com.example.checksheetproject.domain.model.InspectionSubmissionStatus
import com.example.checksheetproject.domain.repository.InspectionDraftRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SaveInspectionDraftUseCaseTest {
    @Test
    fun `점검을 전혀 하지 않은 payload는 임시저장하지 않는다`() = runTest {
        val repository = FakeInspectionDraftRepository()
        val useCase = SaveInspectionDraftUseCase(repository)

        useCase(
            payload = payload(
                items = listOf(
                    item(
                        status = InspectionSubmissionStatus.NOT_SELECTED,
                    ),
                ),
            ),
        )

        assertNull(repository.savedPayload)
    }

    @Test
    fun `선택 상태가 있는 payload는 임시저장한다`() = runTest {
        val repository = FakeInspectionDraftRepository()
        val useCase = SaveInspectionDraftUseCase(repository)
        val payload = payload(
            items = listOf(
                item(
                    status = InspectionSubmissionStatus.NORMAL,
                ),
            ),
        )

        useCase(payload)

        assertEquals(payload, repository.savedPayload)
    }

    @Test
    fun `측정값만 입력된 payload도 임시저장한다`() = runTest {
        val repository = FakeInspectionDraftRepository()
        val useCase = SaveInspectionDraftUseCase(repository)
        val payload = payload(
            items = listOf(
                item(
                    status = InspectionSubmissionStatus.NOT_SELECTED,
                    measurementValue = "220.5",
                ),
            ),
        )

        useCase(payload)

        assertEquals(payload, repository.savedPayload)
    }

    private fun payload(
        items: List<InspectionSubmissionItem>,
    ): InspectionSubmissionPayload {
        return InspectionSubmissionPayload(
            chargerId = "CHB-001",
            inspectionMonth = "2026-06-01",
            inspectorId = "",
            createdAtMillis = 1000L,
            createdAtDateTime = "2026-06-01 10:00:00",
            groups = listOf(
                InspectionSubmissionGroup(
                    category = "외관점검 및 청소",
                    title = "1. 충전기",
                    items = items,
                ),
            ),
        )
    }

    private fun item(
        status: InspectionSubmissionStatus,
        measurementValue: String? = null,
    ): InspectionSubmissionItem {
        return InspectionSubmissionItem(
            itemId = "1-1",
            title = "녹, 방청, 도색상태, 외부스크래치 점검",
            rawText = "1-1 녹, 방청, 도색상태, 외부스크래치 점검",
            status = status,
            measurementValue = measurementValue,
        )
    }

    private class FakeInspectionDraftRepository : InspectionDraftRepository {
        var savedPayload: InspectionSubmissionPayload? = null
            private set

        override suspend fun getDraft(
            chargerId: String,
            inspectionMonth: String,
        ): InspectionSubmissionPayload? = null

        override suspend fun saveDraft(payload: InspectionSubmissionPayload) {
            savedPayload = payload
        }

        override suspend fun deleteDraft(
            chargerId: String,
            inspectionMonth: String,
        ) = Unit

        override suspend fun deleteExpiredDrafts(nowMillis: Long) = Unit
    }
}

package com.example.checksheetproject.data.mapper

import com.example.checksheetproject.data.local.InspectionDraftStatusEntity
import com.example.checksheetproject.domain.model.InspectionSubmissionGroup
import com.example.checksheetproject.domain.model.InspectionSubmissionItem
import com.example.checksheetproject.domain.model.InspectionSubmissionPayload
import com.example.checksheetproject.domain.model.InspectionSubmissionStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class InspectionDraftMapperTest {
    @Test
    fun `도메인 점검 내용을 로컬 임시저장 모델로 변환한다`() {
        val draftEntity = payload().toDraftEntity()
        val draftItem = draftEntity.groups.first().items.first()

        assertEquals("CHB-001", draftEntity.chargerId)
        assertEquals("2026-06", draftEntity.inspectionMonth)
        assertEquals("INSPECTOR-001", draftEntity.inspectorId)
        assertEquals(InspectionDraftStatusEntity.ISSUE, draftItem.status)
        assertEquals("커넥터 파손", draftItem.issueMemo)
    }

    @Test
    fun `로컬 임시저장 모델을 도메인 점검 내용으로 변환한다`() {
        val payload = payload().toDraftEntity().toDomain()
        val payloadItem = payload.groups.first().items.first()

        assertEquals("CHB-001", payload.chargerId)
        assertEquals("2026-06", payload.inspectionMonth)
        assertEquals("INSPECTOR-001", payload.inspectorId)
        assertEquals(InspectionSubmissionStatus.ISSUE, payloadItem.status)
        assertEquals("커넥터 파손", payloadItem.issueMemo)
    }

    private fun payload(): InspectionSubmissionPayload {
        return InspectionSubmissionPayload(
            chargerId = "CHB-001",
            inspectionMonth = "2026-06",
            inspectorId = "INSPECTOR-001",
            createdAtMillis = 1_780_000_000_000L,
            createdAtDateTime = "2026-06-01 10:00:00",
            groups = listOf(
                InspectionSubmissionGroup(
                    category = "외관점검 및 청소",
                    title = "1. 충전기",
                    items = listOf(
                        InspectionSubmissionItem(
                            itemId = "1-1",
                            title = "커넥터 상태 점검",
                            rawText = "1-1 커넥터 상태 점검",
                            status = InspectionSubmissionStatus.ISSUE,
                            measurementValue = null,
                            issueMemo = "커넥터 파손",
                        ),
                    ),
                ),
            ),
        )
    }
}

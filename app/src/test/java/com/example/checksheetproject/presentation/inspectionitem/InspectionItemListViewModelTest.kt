package com.example.checksheetproject.presentation.inspectionitem

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InspectionItemListViewModelTest {
    @Test
    fun `첫 점검 그룹은 외관점검 및 청소의 충전기 5개 항목이다`() {
        val viewModel = InspectionItemListViewModel()

        val uiState = viewModel.uiState.value

        assertEquals("외관점검 및 청소", uiState.currentGroup?.category)
        assertEquals("1. 충전기", uiState.currentGroup?.title)
        assertEquals(5, uiState.currentGroup?.items?.size)
        assertEquals("1-1 녹, 방청, 도색상태, 외부스크래치 점검", uiState.currentGroup?.items?.first())
        assertEquals(false, uiState.canMovePrevious)
        assertTrue(uiState.canMoveNext)
        assertEquals(false, uiState.isCurrentGroupCompleted)
    }

    @Test
    fun `다음을 누르면 다음 점검 그룹으로 이동한다`() {
        val viewModel = InspectionItemListViewModel()

        viewModel.moveNext()

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.currentGroupIndex)
        assertEquals("2. 분전함", uiState.currentGroup?.title)
        assertEquals(true, uiState.canMovePrevious)
    }

    @Test
    fun `이전을 누르면 이전 점검 그룹으로 이동한다`() {
        val viewModel = InspectionItemListViewModel()

        viewModel.moveNext()
        viewModel.movePrevious()

        val uiState = viewModel.uiState.value
        assertEquals(0, uiState.currentGroupIndex)
        assertEquals("1. 충전기", uiState.currentGroup?.title)
        assertEquals(false, uiState.canMovePrevious)
    }

    @Test
    fun `마지막 점검 그룹에서는 다음으로 이동할 수 없다`() {
        val viewModel = InspectionItemListViewModel()

        repeat(20) {
            viewModel.moveNext()
        }

        val uiState = viewModel.uiState.value
        assertEquals("충전기 작동 상태", uiState.currentGroup?.title)
        assertEquals(true, uiState.canMovePrevious)
        assertEquals(false, uiState.canMoveNext)
    }

    @Test
    fun `초기화하면 첫 점검 그룹으로 돌아간다`() {
        val viewModel = InspectionItemListViewModel()

        viewModel.moveNext()
        viewModel.moveNext()
        viewModel.updateItemStatus("sample", InspectionCheckStatus.Issue)
        viewModel.updateMeasurementValue("measurement", "220")
        viewModel.updateIssueMemo("sample", "이상 있음")
        viewModel.reset()

        val uiState = viewModel.uiState.value
        assertEquals(0, uiState.currentGroupIndex)
        assertEquals("1. 충전기", uiState.currentGroup?.title)
        assertEquals(false, uiState.canMovePrevious)
        assertEquals(true, uiState.canMoveNext)
        assertEquals(emptyMap<String, InspectionCheckStatus>(), uiState.itemStatuses)
        assertEquals(emptyMap<String, String>(), uiState.measurementValues)
        assertEquals(emptyMap<String, String>(), uiState.issueMemos)
    }

    @Test
    fun `점검 항목 상태와 항목별 이상 사항 메모를 업데이트한다`() {
        val viewModel = InspectionItemListViewModel()
        val item = viewModel.uiState.value.currentGroup?.items?.first().orEmpty()

        viewModel.updateItemStatus(item, InspectionCheckStatus.Issue)
        viewModel.updateIssueMemo(item, "외부 스크래치 확인")

        val uiState = viewModel.uiState.value
        assertEquals(InspectionCheckStatus.Issue, uiState.itemStatuses[item])
        assertEquals("외부 스크래치 확인", uiState.issueMemos[item])
    }

    @Test
    fun `이상 상태를 해제하면 항목별 이상 사항 메모를 제거한다`() {
        val viewModel = InspectionItemListViewModel()
        val item = viewModel.uiState.value.currentGroup?.items?.first().orEmpty()

        viewModel.updateItemStatus(item, InspectionCheckStatus.Issue)
        viewModel.updateIssueMemo(item, "외부 스크래치 확인")
        viewModel.updateItemStatus(item, InspectionCheckStatus.Normal)

        val uiState = viewModel.uiState.value
        assertEquals(InspectionCheckStatus.Normal, uiState.itemStatuses[item])
        assertEquals(null, uiState.issueMemos[item])
    }

    @Test
    fun `현재 그룹의 모든 항목을 선택하면 그룹 완료 상태가 된다`() {
        val viewModel = InspectionItemListViewModel()
        val items = viewModel.uiState.value.currentGroup?.items.orEmpty()

        items.forEach { item ->
            viewModel.updateItemStatus(item, InspectionCheckStatus.Normal)
        }

        assertEquals(true, viewModel.uiState.value.isCurrentGroupCompleted)
    }

    @Test
    fun `성능 및 저항 확인 그룹은 측정값을 모두 입력하면 완료 상태가 된다`() {
        val viewModel = InspectionItemListViewModel()
        repeat(8) {
            viewModel.moveNext()
        }
        val items = viewModel.uiState.value.currentGroup?.items.orEmpty()

        items.forEachIndexed { index, item ->
            viewModel.updateMeasurementValue(item, "${index + 1}.0")
        }

        val uiState = viewModel.uiState.value
        assertEquals("성능 및 저항 확인", uiState.currentGroup?.category)
        assertEquals(true, uiState.isCurrentGroupCompleted)
    }

    @Test
    fun `항목별 이상 사항 메모는 그룹 완료 조건에 포함하지 않는다`() {
        val viewModel = InspectionItemListViewModel()
        val items = viewModel.uiState.value.currentGroup?.items.orEmpty()

        items.forEach { item ->
            viewModel.updateItemStatus(item, InspectionCheckStatus.Normal)
        }

        val uiState = viewModel.uiState.value
        assertEquals(emptyMap<String, String>(), uiState.issueMemos)
        assertEquals(true, uiState.isCurrentGroupCompleted)
    }

    @Test
    fun `서버 전송 payload는 전체 점검 그룹과 항목을 포함한다`() {
        val viewModel = InspectionItemListViewModel()

        val payload = viewModel.createSubmissionPayload(
            chargerId = "CHARGER-001",
            inspectionMonth = "2026-05",
            inspectorId = "INSPECTOR-001",
            createdAtMillis = 1000L,
        )

        assertEquals("CHARGER-001", payload.chargerId)
        assertEquals("2026-05", payload.inspectionMonth)
        assertEquals("INSPECTOR-001", payload.inspectorId)
        assertEquals(1000L, payload.createdAtMillis)
        assertEquals(19, payload.createdAtDateTime.length)
        assertEquals('-', payload.createdAtDateTime[4])
        assertEquals('-', payload.createdAtDateTime[7])
        assertEquals(' ', payload.createdAtDateTime[10])
        assertEquals(':', payload.createdAtDateTime[13])
        assertEquals(':', payload.createdAtDateTime[16])
        assertEquals(viewModel.uiState.value.groups.size, payload.groups.size)
        assertEquals("외관점검 및 청소", payload.groups.first().category)
        assertEquals("1. 충전기", payload.groups.first().title)
        assertEquals(5, payload.groups.first().items.size)
        assertEquals("1-1", payload.groups.first().items.first().itemId)
        assertEquals("녹, 방청, 도색상태, 외부스크래치 점검", payload.groups.first().items.first().title)
        assertEquals(
            InspectionSubmissionStatus.NOT_SELECTED,
            payload.groups.first().items.first().status,
        )
    }

    @Test
    fun `서버 전송 payload는 선택 상태와 항목별 메모를 반영한다`() {
        val viewModel = InspectionItemListViewModel()
        val firstGroupItems = viewModel.uiState.value.currentGroup?.items.orEmpty()

        viewModel.updateItemStatus(firstGroupItems[0], InspectionCheckStatus.Normal)
        viewModel.updateItemStatus(firstGroupItems[1], InspectionCheckStatus.Issue)
        viewModel.updateItemStatus(firstGroupItems[2], InspectionCheckStatus.NotApplicable)
        viewModel.updateIssueMemo(firstGroupItems[1], "2번 항목 바닥 균열 확인")

        val payload = viewModel.createSubmissionPayload(createdAtMillis = 1000L)
        val firstGroupPayload = payload.groups.first()

        assertEquals(InspectionSubmissionStatus.NORMAL, firstGroupPayload.items[0].status)
        assertEquals(InspectionSubmissionStatus.ISSUE, firstGroupPayload.items[1].status)
        assertEquals("2번 항목 바닥 균열 확인", firstGroupPayload.items[1].issueMemo)
        assertEquals(InspectionSubmissionStatus.NOT_APPLICABLE, firstGroupPayload.items[2].status)
        assertEquals(InspectionSubmissionStatus.NOT_SELECTED, firstGroupPayload.items[3].status)
        assertFalse(firstGroupPayload.items.any { it.rawText.isBlank() })
    }

    @Test
    fun `서버 전송 payload는 성능 및 저항 확인 측정값을 반영한다`() {
        val viewModel = InspectionItemListViewModel()
        repeat(8) {
            viewModel.moveNext()
        }
        val measurementItem = viewModel.uiState.value.currentGroup?.items?.first().orEmpty()

        viewModel.updateMeasurementValue(measurementItem, "220.5")

        val payload = viewModel.createSubmissionPayload(createdAtMillis = 1000L)
        val measurementGroupPayload = payload.groups.first { it.category == "성능 및 저항 확인" }

        assertEquals("220.5", measurementGroupPayload.items.first().measurementValue)
        assertEquals(InspectionSubmissionStatus.NOT_SELECTED, measurementGroupPayload.items.first().status)
    }
}

package com.example.checksheetproject.presentation.inspectionitem

import com.example.checksheetproject.domain.model.InspectionSubmissionPayload
import com.example.checksheetproject.domain.model.InspectionSubmissionGroup
import com.example.checksheetproject.domain.model.InspectionSubmissionItem
import com.example.checksheetproject.domain.model.InspectionSubmissionStatus
import com.example.checksheetproject.domain.repository.InspectionDraftRepository
import com.example.checksheetproject.domain.repository.InspectionRepository
import com.example.checksheetproject.domain.usecase.DeleteInspectionDraftUseCase
import com.example.checksheetproject.domain.usecase.GetInspectionDraftUseCase
import com.example.checksheetproject.domain.usecase.SaveInspectionDraftUseCase
import com.example.checksheetproject.domain.usecase.SaveInspectionSubmissionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InspectionItemListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `첫 점검 그룹은 외관점검 및 청소의 충전기 5개 항목이다`() {
        val viewModel = createViewModel()

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
        val viewModel = createViewModel()

        viewModel.moveNext()

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.currentGroupIndex)
        assertEquals("2. 분전함", uiState.currentGroup?.title)
        assertEquals(true, uiState.canMovePrevious)
    }

    @Test
    fun `이전을 누르면 이전 점검 그룹으로 이동한다`() {
        val viewModel = createViewModel()

        viewModel.moveNext()
        viewModel.movePrevious()

        val uiState = viewModel.uiState.value
        assertEquals(0, uiState.currentGroupIndex)
        assertEquals("1. 충전기", uiState.currentGroup?.title)
        assertEquals(false, uiState.canMovePrevious)
    }

    @Test
    fun `마지막 점검 그룹에서는 다음으로 이동할 수 없다`() {
        val viewModel = createViewModel()

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
        val viewModel = createViewModel()

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
        val viewModel = createViewModel()
        val item = viewModel.uiState.value.currentGroup?.items?.first().orEmpty()

        viewModel.updateItemStatus(item, InspectionCheckStatus.Issue)
        viewModel.updateIssueMemo(item, "외부 스크래치 확인")

        val uiState = viewModel.uiState.value
        assertEquals(InspectionCheckStatus.Issue, uiState.itemStatuses[item])
        assertEquals("외부 스크래치 확인", uiState.issueMemos[item])
    }

    @Test
    fun `이상 상태를 해제하면 항목별 이상 사항 메모를 제거한다`() {
        val viewModel = createViewModel()
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
        val viewModel = createViewModel()
        val items = viewModel.uiState.value.currentGroup?.items.orEmpty()

        items.forEach { item ->
            viewModel.updateItemStatus(item, InspectionCheckStatus.Normal)
        }

        assertEquals(true, viewModel.uiState.value.isCurrentGroupCompleted)
    }

    @Test
    fun `성능 및 저항 확인 그룹은 측정값을 모두 입력하면 완료 상태가 된다`() {
        val viewModel = createViewModel()
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
        val viewModel = createViewModel()
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
        val viewModel = createViewModel()

        val payload = viewModel.createSubmissionPayload(
            chargerId = "CHARGER-001",
            inspectionMonth = "2026-05-01",
            inspectorId = "INSPECTOR-001",
            createdAtMillis = 1000L,
        )

        assertEquals("CHARGER-001", payload.chargerId)
        assertEquals("2026-05-01", payload.inspectionMonth)
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
        val viewModel = createViewModel()
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
        val viewModel = createViewModel()
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

    @Test
    fun `작성 중인 점검 항목이 있으면 화면 이탈 전에 임시저장한다`() {
        val draftRepository = FakeInspectionDraftRepository()
        val viewModel = createViewModel(inspectionDraftRepository = draftRepository)
        val firstItem = viewModel.uiState.value.currentGroup?.items?.first().orEmpty()
        var didLeave = false

        viewModel.updateItemStatus(firstItem, InspectionCheckStatus.Issue)
        viewModel.updateIssueMemo(firstItem, "외부 스크래치 확인")
        viewModel.saveDraftBeforeLeaving(
            chargerId = "CHB-001",
            inspectionMonth = "2026-05-01",
            onSaved = { didLeave = true },
        )
        testDispatcher.scheduler.advanceUntilIdle()

        val savedPayload = draftRepository.savedPayload
        assertEquals(true, didLeave)
        assertEquals("CHB-001", savedPayload?.chargerId)
        assertEquals("2026-05-01", savedPayload?.inspectionMonth)
        assertEquals(
            InspectionSubmissionStatus.ISSUE,
            savedPayload?.groups?.first()?.items?.first()?.status,
        )
        assertEquals(
            "외부 스크래치 확인",
            savedPayload?.groups?.first()?.items?.first()?.issueMemo,
        )
    }

    @Test
    fun `기기번호와 점검월이 같은 임시저장 데이터가 있으면 점검 상태를 복원한다`() {
        val firstItem = "1-1 녹, 방청, 도색상태, 외부스크래치 점검"
        val measurementItem = "가. 충전기 입력전압 측정"
        val draftRepository = FakeInspectionDraftRepository(
            draftPayload = InspectionSubmissionPayload(
                chargerId = "CHB-001",
                inspectionMonth = "2026-05-01",
                inspectorId = "",
                createdAtMillis = 1000L,
                createdAtDateTime = "2026-05-01 10:00:00",
                groups = listOf(
                    InspectionSubmissionGroup(
                        category = "외관점검 및 청소",
                        title = "1. 충전기",
                        items = listOf(
                            InspectionSubmissionItem(
                                itemId = "1-1",
                                title = "녹, 방청, 도색상태, 외부스크래치 점검",
                                rawText = firstItem,
                                status = InspectionSubmissionStatus.ISSUE,
                                issueMemo = "외부 스크래치 확인",
                            ),
                        ),
                    ),
                    InspectionSubmissionGroup(
                        category = "성능 및 저항 확인",
                        title = "전기 성능 측정",
                        items = listOf(
                            InspectionSubmissionItem(
                                itemId = "가.",
                                title = "충전기 입력전압 측정",
                                rawText = measurementItem,
                                status = InspectionSubmissionStatus.NOT_SELECTED,
                                measurementValue = "220.5",
                            ),
                        ),
                    ),
                ),
            ),
        )
        val viewModel = createViewModel(inspectionDraftRepository = draftRepository)

        viewModel.loadDraft(
            chargerId = "CHB-001",
            inspectionMonth = "2026-05-01",
        )
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertEquals(InspectionCheckStatus.Issue, uiState.itemStatuses[firstItem])
        assertEquals("외부 스크래치 확인", uiState.issueMemos[firstItem])
        assertEquals("220.5", uiState.measurementValues[measurementItem])
    }

    private fun createViewModel(
        inspectionRepository: InspectionRepository = FakeInspectionRepository(),
        inspectionDraftRepository: InspectionDraftRepository = FakeInspectionDraftRepository(),
    ): InspectionItemListViewModel {
        return InspectionItemListViewModel(
            saveInspectionSubmissionUseCase = SaveInspectionSubmissionUseCase(inspectionRepository),
            saveInspectionDraftUseCase = SaveInspectionDraftUseCase(inspectionDraftRepository),
            deleteInspectionDraftUseCase = DeleteInspectionDraftUseCase(inspectionDraftRepository),
            getInspectionDraftUseCase = GetInspectionDraftUseCase(inspectionDraftRepository),
        )
    }

    private class FakeInspectionRepository : InspectionRepository {
        override suspend fun saveInspection(payload: InspectionSubmissionPayload) = Unit
    }

    private class FakeInspectionDraftRepository(
        private val draftPayload: InspectionSubmissionPayload? = null,
    ) : InspectionDraftRepository {
        var savedPayload: InspectionSubmissionPayload? = null
            private set

        var deletedDraftKey: Pair<String, String>? = null
            private set

        override suspend fun getDraft(
            chargerId: String,
            inspectionMonth: String,
        ): InspectionSubmissionPayload? {
            return draftPayload?.takeIf {
                it.chargerId == chargerId && it.inspectionMonth == inspectionMonth
            }
        }

        override suspend fun saveDraft(payload: InspectionSubmissionPayload) {
            savedPayload = payload
        }

        override suspend fun deleteDraft(
            chargerId: String,
            inspectionMonth: String,
        ) {
            deletedDraftKey = chargerId to inspectionMonth
        }

        override suspend fun deleteExpiredDrafts(nowMillis: Long) = Unit
    }
}

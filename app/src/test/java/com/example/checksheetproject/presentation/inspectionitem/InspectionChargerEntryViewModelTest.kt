package com.example.checksheetproject.presentation.inspectionitem

import com.example.checksheetproject.domain.model.Charger
import com.example.checksheetproject.domain.model.InspectionStatus
import com.example.checksheetproject.domain.model.InspectionSubmissionPayload
import com.example.checksheetproject.domain.repository.ChargerRepository
import com.example.checksheetproject.domain.repository.InspectionDraftRepository
import com.example.checksheetproject.domain.usecase.DeleteInspectionDraftUseCase
import com.example.checksheetproject.domain.usecase.GetChargerUseCase
import com.example.checksheetproject.domain.usecase.GetInspectionDraftUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InspectionChargerEntryViewModelTest {
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
    fun `충전기 ID를 입력하면 확인 가능 상태가 된다`() {
        val viewModel = createViewModel()

        viewModel.updateChargerIdInput("CHB-001")

        val uiState = viewModel.uiState.value
        assertEquals("CHB-001", uiState.chargerIdInput)
        assertTrue(uiState.canCheckCharger)
        assertNull(uiState.chargerInfo)
    }

    @Test
    fun `빈 충전기 ID로 확인하면 오류 메시지를 표시한다`() {
        val viewModel = createViewModel()

        viewModel.checkChargerInfo()

        val uiState = viewModel.uiState.value
        assertEquals("충전기 ID를 입력하세요.", uiState.errorMessage)
        assertFalse(uiState.isChecking)
        assertNull(uiState.chargerInfo)
    }

    @Test
    fun `충전기 정보 확인을 누르면 확인된 충전기 정보를 표시한다`() {
        val viewModel = createViewModel()

        viewModel.updateChargerIdInput(" CHB-001 ")
        viewModel.checkChargerInfo()
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isChecking)
        assertEquals("CHB-001", uiState.chargerInfo?.id)
        assertEquals("충전기 CHB-001", uiState.chargerInfo?.name)
        assertNull(uiState.errorMessage)
    }

    @Test
    fun `임시저장 데이터가 있으면 API 조회 없이 충전기 정보를 표시한다`() {
        val chargerRepository = FakeChargerRepository(errorOnLoad = true)
        val viewModel = createViewModel(
            chargerRepository = chargerRepository,
            inspectionDraftRepository = FakeInspectionDraftRepository(
                draftPayload = draftPayload(chargerId = "CHB-001"),
            ),
        )

        viewModel.updateChargerIdInput("CHB-001")
        viewModel.checkChargerInfo()
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isChecking)
        assertEquals("CHB-001", uiState.chargerInfo?.id)
        assertEquals("임시저장된 점검 데이터", uiState.chargerInfo?.location)
        assertTrue(uiState.chargerInfo?.hasDraft == true)
        assertEquals(0, chargerRepository.loadCount)
        assertNull(uiState.errorMessage)
    }

    @Test
    fun `임시저장 데이터가 없으면 API 충전기 데이터를 조회한다`() {
        val chargerRepository = FakeChargerRepository()
        val viewModel = createViewModel(chargerRepository = chargerRepository)

        viewModel.updateChargerIdInput("CHB-001")
        viewModel.checkChargerInfo()
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertEquals("충전기 CHB-001", uiState.chargerInfo?.name)
        assertEquals("서울 강남구", uiState.chargerInfo?.location)
        assertEquals(1, chargerRepository.loadCount)
    }

    @Test
    fun `임시저장 데이터가 있으면 점검 시작 시 불러오기 확인 팝업을 표시한다`() {
        val viewModel = createViewModel(
            inspectionDraftRepository = FakeInspectionDraftRepository(
                draftPayload = draftPayload(chargerId = "CHB-001"),
            ),
        )
        var startedChargerId: String? = null

        viewModel.updateChargerIdInput("CHB-001")
        viewModel.checkChargerInfo()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.requestStartInspection { startedChargerId = it }

        assertTrue(viewModel.uiState.value.showDraftLoadDialog)
        assertNull(startedChargerId)
    }

    @Test
    fun `임시저장 데이터 불러오기 확인을 누르면 삭제 없이 점검을 시작한다`() {
        val draftRepository = FakeInspectionDraftRepository(
            draftPayload = draftPayload(chargerId = "CHB-001"),
        )
        val viewModel = createViewModel(inspectionDraftRepository = draftRepository)
        var startedChargerId: String? = null

        viewModel.updateChargerIdInput("CHB-001")
        viewModel.checkChargerInfo()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.requestStartInspection { startedChargerId = it }
        viewModel.startInspectionWithDraft { startedChargerId = it }

        assertEquals("CHB-001", startedChargerId)
        assertFalse(viewModel.uiState.value.showDraftLoadDialog)
        assertNull(draftRepository.deletedDraftKey)
    }

    @Test
    fun `임시저장 데이터 불러오기 아니오를 누르면 삭제 후 처음부터 점검을 시작한다`() {
        val draftRepository = FakeInspectionDraftRepository(
            draftPayload = draftPayload(chargerId = "CHB-001"),
        )
        val viewModel = createViewModel(inspectionDraftRepository = draftRepository)
        var startedChargerId: String? = null

        viewModel.updateChargerIdInput("CHB-001")
        viewModel.checkChargerInfo()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.requestStartInspection { startedChargerId = it }
        viewModel.startInspectionWithoutDraft { startedChargerId = it }
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("CHB-001", startedChargerId)
        assertEquals("CHB-001", draftRepository.deletedDraftKey?.first)
        assertFalse(viewModel.uiState.value.showDraftLoadDialog)
        assertFalse(viewModel.uiState.value.isStartingInspection)
    }

    @Test
    fun `초기화하면 입력값과 확인된 충전기 정보를 지운다`() {
        val viewModel = createViewModel(
            inspectionDraftRepository = FakeInspectionDraftRepository(
                draftPayload = draftPayload(chargerId = "CHB-001"),
            ),
        )

        viewModel.updateChargerIdInput("CHB-001")
        viewModel.checkChargerInfo()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.requestStartInspection {}
        viewModel.reset()

        val uiState = viewModel.uiState.value
        assertEquals("", uiState.chargerIdInput)
        assertNull(uiState.chargerInfo)
        assertFalse(uiState.showDraftLoadDialog)
        assertFalse(uiState.isChecking)
        assertFalse(uiState.isStartingInspection)
        assertNull(uiState.errorMessage)
    }

    private fun createViewModel(
        chargerRepository: FakeChargerRepository = FakeChargerRepository(),
        inspectionDraftRepository: InspectionDraftRepository = FakeInspectionDraftRepository(),
    ): InspectionChargerEntryViewModel {
        return InspectionChargerEntryViewModel(
            getInspectionDraftUseCase = GetInspectionDraftUseCase(inspectionDraftRepository),
            getChargerUseCase = GetChargerUseCase(chargerRepository),
            deleteInspectionDraftUseCase = DeleteInspectionDraftUseCase(inspectionDraftRepository),
        )
    }

    private class FakeChargerRepository(
        private val errorOnLoad: Boolean = false,
    ) : ChargerRepository {
        var loadCount = 0
            private set

        override suspend fun getChargers(): List<Charger> {
            loadCount += 1
            if (errorOnLoad) error("API should not be called.")
            return listOf(
                Charger(
                    id = "CHB-001",
                    name = "충전기 CHB-001",
                    location = "서울 강남구",
                    monthlySalesRankPercent = 3,
                    inspectionStatus = InspectionStatus.NotInspected,
                ),
            )
        }
    }

    private class FakeInspectionDraftRepository(
        private val draftPayload: InspectionSubmissionPayload? = null,
    ) : InspectionDraftRepository {
        var deletedDraftKey: Pair<String, String>? = null
            private set

        override suspend fun getDraft(
            chargerId: String,
            inspectionMonth: String,
        ): InspectionSubmissionPayload? {
            return draftPayload?.takeIf { it.chargerId == chargerId }
        }

        override suspend fun saveDraft(payload: InspectionSubmissionPayload) = Unit

        override suspend fun deleteDraft(
            chargerId: String,
            inspectionMonth: String,
        ) {
            deletedDraftKey = chargerId to inspectionMonth
        }

        override suspend fun deleteExpiredDrafts(nowMillis: Long) = Unit
    }

    private fun draftPayload(chargerId: String): InspectionSubmissionPayload {
        return InspectionSubmissionPayload(
            chargerId = chargerId,
            inspectionMonth = "2026-06",
            inspectorId = "",
            createdAtMillis = 1000L,
            createdAtDateTime = "2026-06-01 10:00:00",
            groups = emptyList(),
        )
    }
}

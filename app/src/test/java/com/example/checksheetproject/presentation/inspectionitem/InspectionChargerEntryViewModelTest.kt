package com.example.checksheetproject.presentation.inspectionitem

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
        val viewModel = InspectionChargerEntryViewModel()

        viewModel.updateChargerIdInput("CHB-001")

        val uiState = viewModel.uiState.value
        assertEquals("CHB-001", uiState.chargerIdInput)
        assertTrue(uiState.canCheckCharger)
        assertNull(uiState.chargerInfo)
    }

    @Test
    fun `빈 충전기 ID로 확인하면 오류 메시지를 표시한다`() {
        val viewModel = InspectionChargerEntryViewModel()

        viewModel.checkChargerInfo()

        val uiState = viewModel.uiState.value
        assertEquals("충전기 ID를 입력하세요.", uiState.errorMessage)
        assertFalse(uiState.isChecking)
        assertNull(uiState.chargerInfo)
    }

    @Test
    fun `충전기 정보 확인을 누르면 확인된 충전기 정보를 표시한다`() {
        val viewModel = InspectionChargerEntryViewModel()

        viewModel.updateChargerIdInput(" CHB-001 ")
        viewModel.checkChargerInfo()
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isChecking)
        assertEquals("CHB-001", uiState.chargerInfo?.id)
        assertEquals("충전기 CHB-001", uiState.chargerInfo?.name)
        assertNull(uiState.errorMessage)
    }
}

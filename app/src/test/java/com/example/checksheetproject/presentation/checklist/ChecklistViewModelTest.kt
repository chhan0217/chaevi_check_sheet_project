package com.example.checksheetproject.presentation.checklist

import org.junit.Assert.assertEquals
import org.junit.Test

class ChecklistViewModelTest {
    @Test
    fun `점검표를 불러오면 충전기와 점검월이 UI 상태에 반영된다`() {
        val viewModel = ChecklistViewModel()

        viewModel.loadChecklist(
            chargerId = "CHB-001",
            inspectionMonth = "2026-05",
        )

        val uiState = viewModel.uiState.value
        assertEquals("CHB-001", uiState.chargerId)
        assertEquals("2026-05", uiState.inspectionMonth)
    }

    @Test
    fun `점검 항목 상태를 변경하면 해당 항목만 업데이트된다`() {
        val viewModel = ChecklistViewModel()
        val firstItem = viewModel.uiState.value.items.first()
        val secondItem = viewModel.uiState.value.items[1]

        viewModel.updateItemStatus(
            itemId = firstItem.id,
            status = CheckStatus.Warning,
        )

        val items = viewModel.uiState.value.items
        assertEquals(CheckStatus.Warning, items.first { it.id == firstItem.id }.selectedStatus)
        assertEquals(secondItem.selectedStatus, items.first { it.id == secondItem.id }.selectedStatus)
    }

    @Test
    fun `메모를 변경하면 UI 상태에 반영된다`() {
        val viewModel = ChecklistViewModel()

        viewModel.updateMemo("커넥터 손상 확인")

        assertEquals("커넥터 손상 확인", viewModel.uiState.value.memo)
    }
}

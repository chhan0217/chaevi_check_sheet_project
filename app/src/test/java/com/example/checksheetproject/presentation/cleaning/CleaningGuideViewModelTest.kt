package com.example.checksheetproject.presentation.cleaning

import org.junit.Assert.assertEquals
import org.junit.Test

class CleaningGuideViewModelTest {
    @Test
    fun `청소 관리 화면은 두 개 섹션을 제공한다`() {
        val viewModel = CleaningGuideViewModel()

        val sectionTitles = viewModel.uiState.value.sections.map { it.title }

        assertEquals(
            listOf(
                "1. 충전기 청소의 항목별 기준",
                "2. 청소방법 사례",
            ),
            sectionTitles,
        )
    }

    @Test
    fun `충전기 청소의 항목별 기준 내용을 제공한다`() {
        val viewModel = CleaningGuideViewModel()

        val firstSection = viewModel.uiState.value.sections.first()
        val categories = firstSection.tableRows.map { it.category }.distinct()
        val cautions = firstSection.tableRows.map { it.caution }

        assertEquals(
            listOf("외함 및 충전기 주변", "내부청소", "건함 및 커플러"),
            categories,
        )
        assertEquals(
            true,
            cautions.any { it.contains("충전기 전원 반드시 OFF") },
        )
    }

    @Test
    fun `청소방법 사례 이미지는 5개를 제공한다`() {
        val viewModel = CleaningGuideViewModel()

        val images = viewModel.uiState.value.sections[1].images

        assertEquals(
            listOf("temp_1", "temp_2", "temp_3", "temp_4", "temp_5"),
            images.map { it.resourceName },
        )
        assertEquals(
            listOf("거미줄 제거", "잡초 제거", "터치패널오염제거", "건함 및 커플러 오염제거", "충전기 내부 이물제거"),
            images.map { it.title },
        )
    }
}

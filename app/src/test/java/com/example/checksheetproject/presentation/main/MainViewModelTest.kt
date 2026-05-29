package com.example.checksheetproject.presentation.main

import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {
    @Test
    fun `메인 화면은 4개 메뉴를 제공한다`() {
        val viewModel = MainViewModel()

        val menuTypes = viewModel.uiState.value.menuItems.map { it.type }

        assertEquals(
            listOf(
                MainMenuType.FocusedChargers,
                MainMenuType.InspectionItems,
                MainMenuType.CleaningGuide,
                MainMenuType.Settings,
            ),
            menuTypes,
        )
    }
}

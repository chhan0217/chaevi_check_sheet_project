package com.example.checksheetproject.presentation.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        MainUiState(
            menuItems = listOf(
                MainMenuItemUi(
                    type = MainMenuType.FocusedChargers,
                    title = "집중관리 충전기",
                    description = "상위 매출 45% 대상 충전기",
                ),
                MainMenuItemUi(
                    type = MainMenuType.InspectionItems,
                    title = "점검항목 리스트",
                    description = "정기점검 항목 기준표",
                ),
                MainMenuItemUi(
                    type = MainMenuType.CleaningGuide,
                    title = "충전기 청소 및 지속관리 방법",
                    description = "청소 기준과 방법 사례",
                ),
                MainMenuItemUi(
                    type = MainMenuType.Settings,
                    title = "설정",
                    description = "앱 및 배포 정보",
                ),
            ),
        ),
    )

    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
}

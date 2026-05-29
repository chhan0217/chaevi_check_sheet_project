package com.example.checksheetproject.presentation.cleaning

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class CleaningGuideViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        CleaningGuideUiState(
            sections = listOf(
                CleaningGuideSectionUi(
                    title = "1. 충전기 청소의 항목별 기준",
                    description = "",
                    tableRows = listOf(
                        CleaningGuideTableRowUi(
                            category = "외함 및 충전기 주변",
                            managementStandard = "외관상 자연물(거미줄, 꽃가루, 먼지)로 인한 충전기 오염제거",
                            cleaningTool = "보루걸레, 먼지털이개",
                        ),
                        CleaningGuideTableRowUi(
                            category = "외함 및 충전기 주변",
                            managementStandard = "주변 담배꽁초나 쓰레기 청소",
                            cleaningTool = "빗자루",
                        ),
                        CleaningGuideTableRowUi(
                            category = "내부청소",
                            managementStandard = "파워모듈 분리 후 내부먼지 청소",
                            cleaningTool = "송풍기나 콤푸레셔",
                            caution = "충전기 전원 반드시 OFF하고 내부 배선류 및 부품손상 주의",
                        ),
                        CleaningGuideTableRowUi(
                            category = "내부청소",
                            managementStandard = "충전기 내부 먼지청소",
                            cleaningTool = "송풍기나 콤푸레셔",
                        ),
                        CleaningGuideTableRowUi(
                            category = "건함 및 커플러",
                            managementStandard = "외관상 자연물(거미줄, 꽃가루, 먼지)로 인한 충전기 오염제거",
                            cleaningTool = "보루걸레, 물왁스",
                        ),
                    ),
                ),
                CleaningGuideSectionUi(
                    title = "2. 청소방법 사례",
                    description = "",
                    images = listOf(
                        CleaningGuideImageUi(
                            resourceName = "temp_1",
                            title = "거미줄 제거",
                        ),
                        CleaningGuideImageUi(
                            resourceName = "temp_2",
                            title = "잡초 제거",
                        ),
                        CleaningGuideImageUi(
                            resourceName = "temp_3",
                            title = "터치패널오염제거",
                        ),
                        CleaningGuideImageUi(
                            resourceName = "temp_4",
                            title = "건함 및 커플러 오염제거",
                        ),
                        CleaningGuideImageUi(
                            resourceName = "temp_5",
                            title = "충전기 내부 이물제거",
                        ),
                    ),
                ),
            ),
        ),
    )

    val uiState: StateFlow<CleaningGuideUiState> = _uiState.asStateFlow()
}

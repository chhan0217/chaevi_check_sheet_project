package com.example.checksheetproject.presentation.charger

import androidx.lifecycle.ViewModel
import com.example.checksheetproject.presentation.common.InspectionStatusUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ChargerListViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        ChargerListUiState(
            inspectionMonthLabel = "2026년 5월 정기점검",
            chargers = listOf(
                ChargerItemUi(
                    id = "CHB-001",
                    name = "강남센터 급속 01",
                    location = "서울 강남구",
                    monthlySalesRank = "상위 3%",
                    status = InspectionStatusUi.Pending,
                ),
                ChargerItemUi(
                    id = "CHB-014",
                    name = "판교허브 급속 02",
                    location = "경기 성남시",
                    monthlySalesRank = "상위 12%",
                    status = InspectionStatusUi.Warning,
                ),
                ChargerItemUi(
                    id = "CHB-027",
                    name = "수원영업소 완속 01",
                    location = "경기 수원시",
                    monthlySalesRank = "상위 31%",
                    status = InspectionStatusUi.Done,
                ),
            ),
        ),
    )

    val uiState: StateFlow<ChargerListUiState> = _uiState.asStateFlow()
}

package com.example.checksheetproject.presentation.inspectionitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class InspectionChargerEntryViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(InspectionChargerEntryUiState())

    val uiState: StateFlow<InspectionChargerEntryUiState> = _uiState.asStateFlow()

    fun updateChargerIdInput(chargerId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                chargerIdInput = chargerId,
                chargerInfo = null,
                errorMessage = null,
            )
        }
    }

    fun checkChargerInfo() {
        val chargerId = _uiState.value.chargerIdInput.trim()
        if (chargerId.isBlank()) {
            _uiState.update { currentState ->
                currentState.copy(
                    chargerInfo = null,
                    errorMessage = "충전기 ID를 입력하세요.",
                )
            }
            return
        }

        _uiState.update { currentState ->
            currentState.copy(
                isChecking = true,
                chargerInfo = null,
                errorMessage = null,
            )
        }

        viewModelScope.launch {
            val chargerInfo = loadChargerInfo(chargerId)
            _uiState.update { currentState ->
                currentState.copy(
                    isChecking = false,
                    chargerInfo = chargerInfo,
                    errorMessage = null,
                )
            }
        }
    }

    private fun loadChargerInfo(chargerId: String): InspectionChargerInfoUi {
        return InspectionChargerInfoUi(
            id = chargerId,
            name = "충전기 $chargerId",
            location = "확인된 위치 정보",
        )
    }
}

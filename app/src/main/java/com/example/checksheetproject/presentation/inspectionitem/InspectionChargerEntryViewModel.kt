package com.example.checksheetproject.presentation.inspectionitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checksheetproject.domain.model.Charger
import com.example.checksheetproject.domain.model.InspectionSubmissionPayload
import com.example.checksheetproject.domain.usecase.GetChargerUseCase
import com.example.checksheetproject.domain.usecase.GetInspectionDraftUseCase
import com.example.checksheetproject.domain.usecase.DeleteInspectionDraftUseCase
import com.example.checksheetproject.presentation.common.currentInspectionDate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class InspectionChargerEntryViewModel @Inject constructor(
    private val getInspectionDraftUseCase: GetInspectionDraftUseCase,
    private val getChargerUseCase: GetChargerUseCase,
    private val deleteInspectionDraftUseCase: DeleteInspectionDraftUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(InspectionChargerEntryUiState())

    val uiState: StateFlow<InspectionChargerEntryUiState> = _uiState.asStateFlow()

    fun reset() {
        _uiState.value = InspectionChargerEntryUiState()
    }

    fun updateChargerIdInput(chargerId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                chargerIdInput = chargerId,
                chargerInfo = null,
                showDraftLoadDialog = false,
                errorMessage = null,
            )
        }
    }

    fun updateChargerIdFromQrScan(scanContents: String?) {
        val chargerId = scanContents?.trim().orEmpty()
        if (chargerId.isBlank()) return

        updateChargerIdInput(chargerId)
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
            runCatching {
                loadChargerInfo(chargerId)
            }.onSuccess { chargerInfo ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isChecking = false,
                        chargerInfo = chargerInfo,
                        errorMessage = null,
                    )
                }
            }.onFailure { throwable ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isChecking = false,
                        chargerInfo = null,
                        errorMessage = throwable.message ?: "충전기 정보를 확인하지 못했습니다.",
                    )
                }
            }
        }
    }

    fun requestStartInspection(
        onStartInspection: (String) -> Unit,
    ) {
        val chargerInfo = _uiState.value.chargerInfo ?: return
        if (chargerInfo.hasDraft) {
            _uiState.update { currentState ->
                currentState.copy(showDraftLoadDialog = true)
            }
        } else {
            onStartInspection(chargerInfo.id)
        }
    }

    fun dismissDraftLoadDialog() {
        _uiState.update { currentState ->
            currentState.copy(showDraftLoadDialog = false)
        }
    }

    fun startInspectionWithDraft(
        onStartInspection: (String) -> Unit,
    ) {
        val chargerId = _uiState.value.chargerInfo?.id ?: return
        _uiState.update { currentState ->
            currentState.copy(showDraftLoadDialog = false)
        }
        onStartInspection(chargerId)
    }

    fun startInspectionWithoutDraft(
        onStartInspection: (String) -> Unit,
    ) {
        val chargerId = _uiState.value.chargerInfo?.id ?: return
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isStartingInspection = true,
                    showDraftLoadDialog = false,
                    errorMessage = null,
                )
            }
            runCatching {
                deleteInspectionDraftUseCase(
                    chargerId = chargerId,
                    inspectionMonth = currentInspectionMonth(),
                )
            }.onSuccess {
                _uiState.update { currentState ->
                    currentState.copy(isStartingInspection = false)
                }
                onStartInspection(chargerId)
            }.onFailure { throwable ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isStartingInspection = false,
                        errorMessage = throwable.message ?: "임시저장 데이터를 삭제하지 못했습니다.",
                    )
                }
            }
        }
    }

    private suspend fun loadChargerInfo(chargerId: String): InspectionChargerInfoUi {
        val draft = getInspectionDraftUseCase(
            chargerId = chargerId,
            inspectionMonth = currentInspectionMonth(),
        )
        if (draft != null) {
            return draft.toChargerInfoUi()
        }

        val charger = getChargerUseCase(chargerId)
            ?: error("충전기 정보를 찾을 수 없습니다.")

        return charger.toChargerInfoUi()
    }

    private fun InspectionSubmissionPayload.toChargerInfoUi(): InspectionChargerInfoUi {
        return InspectionChargerInfoUi(
            id = chargerId,
            name = "충전기 $chargerId",
            location = "임시저장된 점검 데이터",
            hasDraft = true,
        )
    }

    private fun Charger.toChargerInfoUi(): InspectionChargerInfoUi {
        return InspectionChargerInfoUi(
            id = id,
            name = name,
            location = location,
        )
    }

    private fun currentInspectionMonth(): String {
        return currentInspectionDate()
    }
}

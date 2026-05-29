package com.example.checksheetproject.presentation.checklist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ChecklistViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        ChecklistUiState(
            items = defaultChecklistItems,
        ),
    )

    val uiState: StateFlow<ChecklistUiState> = _uiState.asStateFlow()

    fun loadChecklist(
        chargerId: String,
        inspectionMonth: String,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                chargerId = chargerId,
                inspectionMonth = inspectionMonth,
            )
        }
    }

    fun updateItemStatus(
        itemId: String,
        status: CheckStatus,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                items = currentState.items.map { item ->
                    if (item.id == itemId) {
                        item.copy(selectedStatus = status)
                    } else {
                        item
                    }
                },
            )
        }
    }

    fun updateMemo(memo: String) {
        _uiState.update { currentState ->
            currentState.copy(memo = memo)
        }
    }

    private companion object {
        val defaultChecklistItems = listOf(
            ChecklistItemUi(
                id = "appearance",
                title = "외관 및 파손 여부",
                selectedStatus = CheckStatus.Normal,
            ),
            ChecklistItemUi(
                id = "connector",
                title = "충전 커넥터 상태",
                selectedStatus = CheckStatus.Normal,
            ),
            ChecklistItemUi(
                id = "display-payment",
                title = "화면 및 결제 동작",
                selectedStatus = CheckStatus.Normal,
            ),
            ChecklistItemUi(
                id = "network",
                title = "통신 상태",
                selectedStatus = CheckStatus.Normal,
            ),
            ChecklistItemUi(
                id = "emergency-stop",
                title = "비상정지 버튼 상태",
                selectedStatus = CheckStatus.Normal,
            ),
        )
    }
}

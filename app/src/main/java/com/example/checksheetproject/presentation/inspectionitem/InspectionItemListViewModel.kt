package com.example.checksheetproject.presentation.inspectionitem

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class InspectionItemListViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        InspectionItemListUiState(
            groups = inspectionGroups,
            canMoveNext = inspectionGroups.size > 1,
        ),
    )

    val uiState: StateFlow<InspectionItemListUiState> = _uiState.asStateFlow()

    fun reset() {
        _uiState.update { currentState ->
            currentState.copy(
                currentGroupIndex = 0,
                canMovePrevious = false,
                canMoveNext = currentState.groups.size > 1,
                itemStatuses = emptyMap(),
                measurementValues = emptyMap(),
                issueMemos = emptyMap(),
            )
        }
    }

    fun updateItemStatus(
        item: String,
        status: InspectionCheckStatus,
    ) {
        _uiState.update { currentState ->
            val nextIssueMemos = if (status == InspectionCheckStatus.Issue) {
                currentState.issueMemos
            } else {
                currentState.issueMemos - item
            }
            currentState.copy(
                itemStatuses = currentState.itemStatuses + (item to status),
                issueMemos = nextIssueMemos,
            )
        }
    }

    fun updateMeasurementValue(
        item: String,
        value: String,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                measurementValues = currentState.measurementValues + (item to value),
            )
        }
    }

    fun updateIssueMemo(
        item: String,
        memo: String,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                issueMemos = currentState.issueMemos + (item to memo),
            )
        }
    }

    fun createSubmissionPayload(
        chargerId: String = "",
        inspectionMonth: String = "",
        inspectorId: String = "",
        createdAtMillis: Long = System.currentTimeMillis(),
    ): InspectionSubmissionPayload {
        val currentState = _uiState.value

        return InspectionSubmissionPayload(
            chargerId = chargerId,
            inspectionMonth = inspectionMonth,
            inspectorId = inspectorId,
            createdAtMillis = createdAtMillis,
            createdAtDateTime = createdAtMillis.toCreatedAtDateTime(),
            groups = currentState.groups.map { group ->
                InspectionSubmissionGroup(
                    category = group.category,
                    title = group.title,
                    items = group.items.map { item ->
                        InspectionSubmissionItem(
                            itemId = item.toInspectionItemId(),
                            title = item.toInspectionItemTitle(),
                            rawText = item,
                            status = currentState.itemStatuses[item].toSubmissionStatus(),
                            measurementValue = currentState.measurementValues[item],
                            issueMemo = currentState.issueMemos[item],
                        )
                    },
                )
            },
        )
    }

    fun movePrevious() {
        _uiState.update { currentState ->
            val previousIndex = (currentState.currentGroupIndex - 1)
                .coerceAtLeast(0)
            currentState.copy(
                currentGroupIndex = previousIndex,
                canMovePrevious = previousIndex > 0,
                canMoveNext = previousIndex < currentState.groups.lastIndex,
            )
        }
    }

    fun moveNext() {
        _uiState.update { currentState ->
            val nextIndex = (currentState.currentGroupIndex + 1)
                .coerceAtMost(currentState.groups.lastIndex)
            currentState.copy(
                currentGroupIndex = nextIndex,
                canMovePrevious = nextIndex > 0,
                canMoveNext = nextIndex < currentState.groups.lastIndex,
            )
        }
    }

    private companion object {
        fun InspectionCheckStatus?.toSubmissionStatus(): InspectionSubmissionStatus =
            when (this) {
                InspectionCheckStatus.Normal -> InspectionSubmissionStatus.NORMAL
                InspectionCheckStatus.Issue -> InspectionSubmissionStatus.ISSUE
                InspectionCheckStatus.NotApplicable -> InspectionSubmissionStatus.NOT_APPLICABLE
                null -> InspectionSubmissionStatus.NOT_SELECTED
            }

        fun String.toInspectionItemId(): String = substringBefore(
            delimiter = " ",
            missingDelimiterValue = this,
        )

        fun String.toInspectionItemTitle(): String = substringAfter(
            delimiter = " ",
            missingDelimiterValue = this,
        )

        fun Long.toCreatedAtDateTime(): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
            return formatter.format(Date(this))
        }

        val inspectionGroups = listOf(
            InspectionItemGroupUi(
                category = "외관점검 및 청소",
                title = "1. 충전기",
                items = listOf(
                    "1-1 녹, 방청, 도색상태, 외부스크래치 점검",
                    "1-2 바닥 침수 및 균열 점검",
                    "1-3 시방규격에 맞는 부품 점검(모뎀 단말기 등)",
                    "1-4 충전부 과열에 의한 변색 여부 확인",
                    "1-5 기초패드 및 스탠드폴 등의 파손 등으로 인한 충전기 고정상태 확인",
                ),
            ),
            InspectionItemGroupUi(
                category = "외관점검 및 청소",
                title = "2. 분전함",
                items = listOf(
                    "2-1 침수 및 물고임 점검",
                    "2-2 차단기 정상 작동상태 확인",
                    "2-3 배선, 전기결선, 전선피복 상태 확인",
                    "2-4 시건 상태 확인 및 안전 전기 위험 표시",
                    "2-5 전력량계 정상 동작 확인",
                    "2-6 분전함 바닥 균열 및 기울어짐 확인",
                ),
            ),
            InspectionItemGroupUi(
                category = "외관점검 및 청소",
                title = "3. 커넥터(커플러)",
                items = listOf(
                    "3-1 커넥터 균열 및 파손 상태 점검",
                    "3-2 커넥터 외부 스크래치 및 오염여부 점검",
                    "3-3 커넥터와 차량 인렛부 도킹 점검",
                    "3-4 커넥터 보관함 상태",
                ),
            ),
            InspectionItemGroupUi(
                category = "외관점검 및 청소",
                title = "4. LCD/터치패널",
                items = listOf(
                    "4-1 LCD 화면 백화현상 점검",
                    "4-2 화면터치 불량 및 화면 멈춤 점검",
                    "4-3 UI 뒤집힘 및 윈도우화면 전환 현상 점검",
                ),
            ),
            InspectionItemGroupUi(
                category = "외관점검 및 청소",
                title = "5. 캐노피",
                items = listOf(
                    "5-1 녹, 방청, 도색상태, 외부 스크래치 점검",
                    "5-2 조명설치 유무 점검",
                ),
            ),
            InspectionItemGroupUi(
                category = "외관점검 및 청소",
                title = "6. 주차구역",
                items = listOf(
                    "6-1 볼라드 및 스토퍼 볼트 체결 상태 점검",
                    "6-2 충전구역 크기 점검",
                    "6-3 전기차 충전구역 바닥 도색 점검",
                ),
            ),
            InspectionItemGroupUi(
                category = "외관점검 및 청소",
                title = "7. CCTV",
                items = listOf(
                    "7-1 CCTV 설치 유무 및 촬영 방향 확인",
                ),
            ),
            InspectionItemGroupUi(
                category = "외관점검 및 청소",
                title = "8. 청소",
                items = listOf(
                    "8-1 외함 및 충전기 주변 청소",
                    "8-2 충전기내부 청소",
                    "8-3 건함 및 커플러 청소",
                ),
            ),
            InspectionItemGroupUi(
                category = "성능 및 저항 확인",
                title = "전기 성능 측정",
                items = listOf(
                    "가. 충전기 입력전압 측정",
                    "나. 접지저항 측정",
                    "다. 절연저항 측정",
                    "라. 누전차단기(ELB) 저압",
                ),
            ),
            InspectionItemGroupUi(
                category = "작동상태점검",
                title = "충전기 작동 상태",
                items = listOf(
                    "가. 전기자동차를 이용하여 충전기 작동 상태를 점검",
                    "나. 충전속도 등 충전기 이상여부 확인",
                    "다. 비상정지버튼의 작동여부 확인",
                ),
            ),
        )
    }
}

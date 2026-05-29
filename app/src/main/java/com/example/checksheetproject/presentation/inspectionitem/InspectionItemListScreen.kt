package com.example.checksheetproject.presentation.inspectionitem

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.checksheetproject.presentation.style.ColorStyles
import com.example.checksheetproject.presentation.style.TextStyles
import com.example.checksheetproject.presentation.theme.CheckSheetTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val InspectionSubmissionLogTag = "CheckSheetInspection"

private val inspectionSubmissionJson = Json {
    encodeDefaults = true
}

@Composable
fun InspectionItemListRoute(
    chargerId: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InspectionItemListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InspectionItemListScreen(
        uiState = uiState,
        onStatusClick = viewModel::updateItemStatus,
        onMeasurementChange = viewModel::updateMeasurementValue,
        onIssueMemoChange = viewModel::updateIssueMemo,
        onPreviousClick = viewModel::movePrevious,
        onNextClick = viewModel::moveNext,
        onSaveClick = {
            val payload = viewModel.createSubmissionPayload(chargerId = chargerId)
            Log.d(InspectionSubmissionLogTag, inspectionSubmissionJson.encodeToString(payload))
            viewModel.reset()
            onSaveClick()
        },
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionItemListScreen(
    uiState: InspectionItemListUiState,
    onStatusClick: (String, InspectionCheckStatus) -> Unit,
    onMeasurementChange: (String, String) -> Unit,
    onIssueMemoChange: (String, String) -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val onNextClickAndScrollTop: () -> Unit = {
        onNextClick()
        coroutineScope.launch {
            listState.scrollToItem(0)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ColorStyles.white,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColorStyles.white,
                    titleContentColor = ColorStyles.black,
                    navigationIconContentColor = ColorStyles.black,
                ),
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(text = "뒤로")
                    }
                },
                title = {
                    Text(text = uiState.title)
                },
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp, bottom = 16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    when {
                        uiState.isFirstGroup -> {
                            InspectionBottomButton(
                                text = "다음",
                                enabled = uiState.isCurrentGroupCompleted,
                                onClick = onNextClickAndScrollTop,
                                modifier = Modifier.weight(1f),
                            )
                        }

                        uiState.isLastGroup -> {
                            InspectionBottomButton(
                                text = "저장",
                                enabled = uiState.isCurrentGroupCompleted,
                                onClick = onSaveClick,
                                modifier = Modifier.weight(1f),
                            )
                        }

                        else -> {
                            InspectionBottomButton(
                                text = "이전",
                                enabled = true,
                                onClick = onPreviousClick,
                                modifier = Modifier.weight(1f),
                            )
                            InspectionBottomButton(
                                text = "다음",
                                enabled = uiState.isCurrentGroupCompleted,
                                onClick = onNextClickAndScrollTop,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                uiState.currentGroup?.let { group ->
                    InspectionGroupHeader(
                        group = group,
                        currentIndex = uiState.currentGroupIndex,
                        totalCount = uiState.groups.size,
                    )
                }
            }
            uiState.currentGroup?.let { group ->
                items(group.items, key = { it }) { item ->
                    InspectionItemCard(
                        item = item,
                        selectedStatus = uiState.itemStatuses[item],
                        measurementValue = uiState.measurementValues[item].orEmpty(),
                        issueMemo = uiState.issueMemos[item].orEmpty(),
                        requiresMeasurementInput = group.requiresMeasurementInput,
                        onStatusClick = { status -> onStatusClick(item, status) },
                        onMeasurementChange = { value -> onMeasurementChange(item, value) },
                        onIssueMemoChange = { memo -> onIssueMemoChange(item, memo) },
                    )
                }
            }
        }
    }
}

@Composable
private fun InspectionBottomButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorStyles.keyColor01Dark02,
            contentColor = ColorStyles.white,
            disabledContainerColor = ColorStyles.grey05,
            disabledContentColor = ColorStyles.grey02,
        ),
    ) {
        Text(
            text = text,
            style = TextStyles.body01.semiBold,
        )
    }
}

@Composable
private fun InspectionGroupHeader(
    group: InspectionItemGroupUi,
    currentIndex: Int,
    totalCount: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = ColorStyles.grey08,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = group.category,
                style = TextStyles.body02.regular,
                color = ColorStyles.grey02,
            )
            Text(
                text = group.title,
                style = TextStyles.header02.semiBold,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${currentIndex + 1} / $totalCount",
                style = TextStyles.body03.medium,
                color = ColorStyles.keyColor01Dark02,
            )
        }
    }
}

@Composable
private fun InspectionItemCard(
    item: String,
    selectedStatus: InspectionCheckStatus?,
    measurementValue: String,
    issueMemo: String,
    requiresMeasurementInput: Boolean,
    onStatusClick: (InspectionCheckStatus) -> Unit,
    onMeasurementChange: (String) -> Unit,
    onIssueMemoChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = ColorStyles.grey08,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = item.substringBefore(" "),
                style = TextStyles.body03.medium,
                color = ColorStyles.keyColor01Dark02,
            )
            Text(
                text = item.substringAfter(" ", item),
                style = TextStyles.body02.regular,
                color = ColorStyles.black,
            )
            if (requiresMeasurementInput) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = measurementValue,
                    onValueChange = { value ->
                        onMeasurementChange(value.filterMeasurementValue())
                    },
                    label = {
                        Text(
                            text = "측정값",
                            style = TextStyles.body03.regular,
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    InspectionCheckStatus.entries.forEach { status ->
                        InspectionStatusButton(
                            status = status,
                            selected = selectedStatus == status,
                            onClick = { onStatusClick(status) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                if (selectedStatus == InspectionCheckStatus.Issue) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = issueMemo,
                        onValueChange = onIssueMemoChange,
                        label = {
                            Text(
                                text = "이상 사항 메모",
                                style = TextStyles.body03.regular,
                            )
                        },
                        minLines = 3,
                    )
                    Text(
                        text = "선택 입력",
                        style = TextStyles.body03.regular,
                        color = ColorStyles.grey02,
                    )
                }
            }
        }
    }
}

@Composable
private fun InspectionStatusButton(
    status: InspectionCheckStatus,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) ColorStyles.keyColor01Dark02 else ColorStyles.white,
            contentColor = if (selected) ColorStyles.white else ColorStyles.grey01,
        ),
    ) {
        Text(
            text = status.label,
            style = TextStyles.body03.semiBold,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InspectionItemListScreenPreview() {
    CheckSheetTheme {
        InspectionItemListScreen(
            uiState = InspectionItemListUiState(
                groups = listOf(
                    InspectionItemGroupUi(
                        category = "외관점검 및 청소",
                        title = "1. 충전기",
                        items = listOf("1-1 녹, 방청, 도색상태, 외부스크래치 점검"),
                    ),
                ),
            ),
            onStatusClick = { _, _ -> },
            onMeasurementChange = { _, _ -> },
            onIssueMemoChange = { _, _ -> },
            onPreviousClick = {},
            onNextClick = {},
            onSaveClick = {},
            onBackClick = {},
        )
    }
}

private fun String.filterMeasurementValue(): String =
    filterIndexed { index, char ->
        char.isDigit() || (char == '.' && indexOf('.') == index)
    }

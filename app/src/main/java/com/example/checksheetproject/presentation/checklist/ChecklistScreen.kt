package com.example.checksheetproject.presentation.checklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.checksheetproject.presentation.style.ColorStyles
import com.example.checksheetproject.presentation.style.TextStyles
import com.example.checksheetproject.presentation.theme.CheckSheetTheme

@Composable
fun ChecklistRoute(
    chargerId: String,
    inspectionMonth: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChecklistViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(chargerId, inspectionMonth) {
        viewModel.loadChecklist(
            chargerId = chargerId,
            inspectionMonth = inspectionMonth,
        )
    }

    ChecklistScreen(
        uiState = uiState,
        onStatusClick = viewModel::updateItemStatus,
        onMemoChange = viewModel::updateMemo,
        onBackClick = onBackClick,
        onSaveClick = {},
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen(
    uiState: ChecklistUiState,
    onStatusClick: (String, CheckStatus) -> Unit,
    onMemoChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ColorStyles.white,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColorStyles.white,
                    titleContentColor = ColorStyles.black,
                    navigationIconContentColor = ColorStyles.black,
                    actionIconContentColor = ColorStyles.black,
                ),
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(text = "뒤로")
                    }
                },
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "정기점검표")
                        Text(
                            text = "${uiState.chargerId} · ${uiState.inspectionMonth}",
                            style = TextStyles.body03.regular,
                            color = ColorStyles.grey02,
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onSaveClick) {
                        Text(text = "저장")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                text = uiState.guideMessage,
                style = TextStyles.body02.regular,
                color = ColorStyles.grey02,
            )

            uiState.items.forEach { item ->
                ChecklistItem(
                    item = item,
                    onStatusClick = onStatusClick,
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.memo,
                onValueChange = onMemoChange,
                label = { Text(text = "이상 사항 메모") },
                minLines = 4,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSaveClick,
            ) {
                Text(text = "점검 결과 저장")
            }
        }
    }
}

@Composable
private fun ChecklistItem(
    item: ChecklistItemUi,
    onStatusClick: (String, CheckStatus) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = item.title,
            style = TextStyles.body01.semiBold,
            fontWeight = FontWeight.SemiBold,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CheckStatus.entries.forEach { status ->
                FilterChip(
                    selected = item.selectedStatus == status,
                    onClick = { onStatusClick(item.id, status) },
                    label = { Text(text = status.label) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChecklistScreenPreview() {
    CheckSheetTheme {
        ChecklistScreen(
            uiState = ChecklistUiState(
                chargerId = "CHB-001",
                inspectionMonth = "2026-05",
                items = listOf(
                    ChecklistItemUi(
                        id = "appearance",
                        title = "외관 및 파손 여부",
                        selectedStatus = CheckStatus.Normal,
                    ),
                ),
            ),
            onStatusClick = { _, _ -> },
            onMemoChange = {},
            onBackClick = {},
            onSaveClick = {},
        )
    }
}

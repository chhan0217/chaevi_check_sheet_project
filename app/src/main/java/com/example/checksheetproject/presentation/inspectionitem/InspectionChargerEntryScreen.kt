package com.example.checksheetproject.presentation.inspectionitem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.checksheetproject.presentation.style.ColorStyles
import com.example.checksheetproject.presentation.style.TextStyles
import com.example.checksheetproject.presentation.theme.CheckSheetTheme

@Composable
fun InspectionChargerEntryRoute(
    onBackClick: () -> Unit,
    onConfirmedClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InspectionChargerEntryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InspectionChargerEntryScreen(
        uiState = uiState,
        onChargerIdChange = viewModel::updateChargerIdInput,
        onCheckClick = viewModel::checkChargerInfo,
        onBackClick = onBackClick,
        onConfirmedClick = onConfirmedClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionChargerEntryScreen(
    uiState: InspectionChargerEntryUiState,
    onChargerIdChange: (String) -> Unit,
    onCheckClick: () -> Unit,
    onBackClick: () -> Unit,
    onConfirmedClick: (String) -> Unit,
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
                ),
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(text = "뒤로")
                    }
                },
                title = {
                    Text(text = "충전기 ID 확인")
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = ColorStyles.grey08,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "점검할 충전기 ID를 입력하세요.",
                        style = TextStyles.header02.semiBold,
                        fontWeight = FontWeight.SemiBold,
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.chargerIdInput,
                        onValueChange = onChargerIdChange,
                        label = {
                            Text(
                                text = "충전기 ID",
                                style = TextStyles.body03.regular,
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                        ),
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.canCheckCharger,
                        onClick = onCheckClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorStyles.keyColor01Dark02,
                            contentColor = ColorStyles.white,
                            disabledContainerColor = ColorStyles.grey05,
                            disabledContentColor = ColorStyles.grey02,
                        ),
                    ) {
                        Text(
                            text = if (uiState.isChecking) "확인 중" else "충전기 정보 확인",
                            style = TextStyles.body01.semiBold,
                        )
                    }
                    uiState.errorMessage?.let { message ->
                        Text(
                            text = message,
                            style = TextStyles.body03.regular,
                            color = ColorStyles.keyColor02,
                        )
                    }
                }
            }

            uiState.chargerInfo?.let { chargerInfo ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(
                        width = 1.dp,
                        color = ColorStyles.keyColor01Dark02,
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = ColorStyles.white,
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = "충전기 정보",
                            style = TextStyles.body02.semiBold,
                            color = ColorStyles.keyColor01Dark02,
                        )
                        InfoRow(
                            label = "충전기 ID",
                            value = chargerInfo.id,
                        )
                        InfoRow(
                            label = "충전기명",
                            value = chargerInfo.name,
                        )
                        InfoRow(
                            label = "위치",
                            value = chargerInfo.location,
                        )
                        InfoRow(
                            label = "확인 상태",
                            value = "확인 완료",
                        )
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onConfirmedClick(chargerInfo.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ColorStyles.keyColor01Dark02,
                                contentColor = ColorStyles.white,
                            ),
                        ) {
                            Text(
                                text = "점검 시작",
                                style = TextStyles.body01.semiBold,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = TextStyles.body03.regular,
            color = ColorStyles.grey02,
        )
        Text(
            text = value,
            style = TextStyles.body03.semiBold,
            color = ColorStyles.black,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InspectionChargerEntryScreenPreview() {
    CheckSheetTheme {
        InspectionChargerEntryScreen(
            uiState = InspectionChargerEntryUiState(
                chargerIdInput = "CHB-001",
                chargerInfo = InspectionChargerInfoUi(
                    id = "CHB-001",
                    name = "충전기 CHB-001",
                    location = "확인된 위치 정보",
                ),
            ),
            onChargerIdChange = {},
            onCheckClick = {},
            onBackClick = {},
            onConfirmedClick = {},
        )
    }
}

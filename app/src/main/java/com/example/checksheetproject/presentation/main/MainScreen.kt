package com.example.checksheetproject.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.checksheetproject.presentation.style.ColorStyles
import com.example.checksheetproject.presentation.style.TextStyles
import com.example.checksheetproject.presentation.theme.CheckSheetTheme

@Composable
fun MainRoute(
    onFocusedChargersClick: () -> Unit,
    onInspectionItemsClick: () -> Unit,
    onCleaningGuideClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
        uiState = uiState,
        onMenuClick = { type ->
            when (type) {
                MainMenuType.FocusedChargers -> onFocusedChargersClick()
                MainMenuType.InspectionItems -> onInspectionItemsClick()
                MainMenuType.CleaningGuide -> onCleaningGuideClick()
                MainMenuType.Settings -> onSettingsClick()
            }
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: MainUiState,
    onMenuClick: (MainMenuType) -> Unit,
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
                ),
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = uiState.title)
                        Text(
                            text = uiState.inspectionMonthLabel,
                            style = TextStyles.body03.regular,
                            color = ColorStyles.grey02,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            uiState.menuItems.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    rowItems.forEach { item ->
                        MainMenuCard(
                            item = item,
                            onClick = { onMenuClick(item.type) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MainMenuCard(
    item: MainMenuItemUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        border = BorderStroke(
            width = 1.dp,
            color = ColorStyles.grey05,
        ),
        colors = CardDefaults.cardColors(
            containerColor = ColorStyles.grey08,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = item.title,
                    style = TextStyles.title01.semiBold,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = item.description,
                    style = TextStyles.body02.regular,
                    color = ColorStyles.grey02,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    CheckSheetTheme {
        MainScreen(
            uiState = MainUiState(
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
            onMenuClick = {},
        )
    }
}

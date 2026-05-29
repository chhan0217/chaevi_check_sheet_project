package com.example.checksheetproject.presentation.charger

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.checksheetproject.presentation.common.CVLoadingView
import com.example.checksheetproject.presentation.common.InspectionStatusBadge
import com.example.checksheetproject.presentation.common.InspectionStatusUi
import com.example.checksheetproject.presentation.style.ColorStyles
import com.example.checksheetproject.presentation.style.TextStyles
import com.example.checksheetproject.presentation.theme.CheckSheetTheme

@Composable
fun ChargerListRoute(
    onBackClick: () -> Unit,
    onChargerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChargerListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChargerListScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onChargerClick = onChargerClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChargerListScreen(
    uiState: ChargerListUiState,
    onBackClick: () -> Unit,
    onChargerClick: (String) -> Unit,
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "집중관리 충전기")
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item { Spacer(modifier = Modifier.height(6.dp)) }
            items(uiState.chargers, key = { it.id }) { charger ->
                ChargerCard(
                    charger = charger,
                    onClick = { onChargerClick(charger.id) },
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun ChargerCard(
    charger: ChargerItemUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = ColorStyles.grey08,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = charger.name,
                        style = TextStyles.header02.semiBold,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "${charger.id} · ${charger.location}",
                        style = TextStyles.body02.regular,
                        color = ColorStyles.grey02,
                    )
                }
                InspectionStatusBadge(status = charger.status)
            }
            Text(
                text = "매출 ${charger.monthlySalesRank}",
                style = TextStyles.body02.semiBold,
                color = ColorStyles.keyColor01Dark02,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChargerListScreenPreview() {
    CheckSheetTheme {
        ChargerListScreen(
            uiState = ChargerListUiState(
                inspectionMonthLabel = "2026년 5월 정기점검",
                chargers = listOf(
                    ChargerItemUi(
                        id = "CHB-001",
                        name = "강남센터 급속 01",
                        location = "서울 강남구",
                        monthlySalesRank = "상위 3%",
                        status = InspectionStatusUi.Pending,
                    ),
                ),
            ),
            onChargerClick = {},
            onBackClick = {},
        )
    }
}

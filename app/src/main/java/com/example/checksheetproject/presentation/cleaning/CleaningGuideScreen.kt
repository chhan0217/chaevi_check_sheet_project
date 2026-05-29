package com.example.checksheetproject.presentation.cleaning

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.checksheetproject.R
import com.example.checksheetproject.presentation.style.ColorStyles
import com.example.checksheetproject.presentation.style.TextStyles
import com.example.checksheetproject.presentation.theme.CheckSheetTheme

@Composable
fun CleaningGuideRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CleaningGuideViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CleaningGuideScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleaningGuideScreen(
    uiState: CleaningGuideUiState,
    onBackClick: () -> Unit,
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
                    Text(text = uiState.title)
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(uiState.sections, key = { it.title }) { section ->
                CleaningGuideSectionCard(section = section)
            }
        }
    }
}

@Composable
private fun CleaningGuideSectionCard(
    section: CleaningGuideSectionUi,
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
                text = section.title,
                style = TextStyles.header02.semiBold,
                fontWeight = FontWeight.SemiBold,
            )
            if (section.tableRows.isNotEmpty()) {
                CleaningGuideGroupedRows(rows = section.tableRows)
            } else if (section.images.isNotEmpty()) {
                CleaningGuideImageCarousel(
                    images = section.images,
                )
            } else {
                Text(
                    text = section.description,
                    style = TextStyles.body02.regular,
                    color = ColorStyles.grey02,
                )
            }
        }
    }
}

@Composable
private fun CleaningGuideImageCarousel(
    images: List<CleaningGuideImageUi>,
    modifier: Modifier = Modifier,
) {
    if (images.isEmpty()) return

    val maxCount = images.size
    val pagerState = rememberPagerState(pageCount = { maxCount })
    val currentPage by remember {
        derivedStateOf { pagerState.currentPage }
    }
    var enlargedImage by remember { mutableStateOf<CleaningGuideImageUi?>(null) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.35f),
        ) { page ->
            val image = images[page]
            Image(
                painter = painterResource(id = image.resourceName.toCleaningImageResourceId()),
                contentDescription = image.title,
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorStyles.white, RoundedCornerShape(8.dp))
                    .clickable { enlargedImage = image },
                contentScale = ContentScale.Fit,
            )
        }
        Text(
            text = images[currentPage].title,
            style = TextStyles.body01.semiBold,
            color = ColorStyles.black,
        )
        Text(
            text = "${currentPage + 1} / $maxCount",
            style = TextStyles.body02.semiBold,
            color = ColorStyles.black,
        )
    }

    enlargedImage?.let { image ->
        CleaningGuideImageDialog(
            image = image,
            onDismissRequest = { enlargedImage = null },
        )
    }
}

@DrawableRes
private fun String.toCleaningImageResourceId(): Int {
    return when (this) {
        "temp_1" -> R.drawable.temp_1
        "temp_2" -> R.drawable.temp_2
        "temp_3" -> R.drawable.temp_3
        "temp_4" -> R.drawable.temp_4
        "temp_5" -> R.drawable.temp_5
        else -> R.drawable.temp_1
    }
}

@Composable
private fun CleaningGuideImageDialog(
    image: CleaningGuideImageUi,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorStyles.blackAlpha)
                .clickable(onClick = onDismissRequest)
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Image(
                    painter = painterResource(id = image.resourceName.toCleaningImageResourceId()),
                    contentDescription = image.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.35f)
                        .background(ColorStyles.white, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit,
                )
                Text(
                    text = image.title,
                    style = TextStyles.body01.semiBold,
                    color = ColorStyles.white,
                )
            }
            Text(
                text = "화면을 누르면 닫힙니다",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 30.dp),
                style = TextStyles.body03.regular,
                color = ColorStyles.grey05,
            )
        }
    }
}

@Composable
private fun CleaningGuideGroupedRows(
    rows: List<CleaningGuideTableRowUi>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        rows.groupBy { it.category }.forEach { (category, categoryRows) ->
            CleaningGuideCategoryCard(
                category = category,
                rows = categoryRows,
            )
        }
    }
}

@Composable
private fun CleaningGuideCategoryCard(
    category: String,
    rows: List<CleaningGuideTableRowUi>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = ColorStyles.white,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = category,
            style = TextStyles.body01.semiBold,
            fontWeight = FontWeight.SemiBold,
            color = ColorStyles.keyColor01Dark02,
        )
        rows.forEach { row ->
            CleaningGuideStandardItem(row = row)
        }
    }
}

@Composable
private fun CleaningGuideStandardItem(
    row: CleaningGuideTableRowUi,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = row.managementStandard,
            style = TextStyles.body02.regular,
            color = ColorStyles.black,
        )
        Text(
            text = "도구: ${row.cleaningTool}",
            style = TextStyles.body03.regular,
            color = ColorStyles.grey02,
        )
        if (row.caution.isNotBlank()) {
            Text(
                text = "주의: ${row.caution}",
                style = TextStyles.body03.regular,
                color = ColorStyles.error,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CleaningGuideScreenPreview() {
    CheckSheetTheme {
        CleaningGuideScreen(
            uiState = CleaningGuideUiState(
                sections = listOf(
                    CleaningGuideSectionUi(
                        title = "1. 충전기 청소의 항목별 기준",
                        description = "",
                        tableRows = listOf(
                            CleaningGuideTableRowUi(
                                category = "외함 및 충전기 주변",
                                managementStandard = "주변 담배꽁초나 쓰레기 청소",
                                cleaningTool = "빗자루",
                            ),
                        ),
                    ),
                    CleaningGuideSectionUi(
                        title = "2. 청소방법 사례",
                        description = "",
                        images = listOf(
                            CleaningGuideImageUi("temp_1", "거미줄 제거"),
                            CleaningGuideImageUi("temp_2", "잡초 제거"),
                            CleaningGuideImageUi("temp_3", "터치패널오염제거"),
                            CleaningGuideImageUi("temp_4", "건함 및 커플러 오염제거"),
                            CleaningGuideImageUi("temp_5", "충전기 내부 이물제거"),
                        ),
                    ),
                ),
            ),
            onBackClick = {},
        )
    }
}

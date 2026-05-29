package com.example.checksheetproject.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.checksheetproject.presentation.style.ColorStyles
import com.example.checksheetproject.presentation.style.TextStyles

enum class InspectionStatusUi(
    val label: String,
) {
    Pending("미점검"),
    Warning("이상 있음"),
    Done("점검 완료"),
}

@Composable
fun InspectionStatusBadge(
    status: InspectionStatusUi,
    modifier: Modifier = Modifier,
) {
    val containerColor = when (status) {
        InspectionStatusUi.Pending -> ColorStyles.grey06
        InspectionStatusUi.Warning -> ColorStyles.reward01
        InspectionStatusUi.Done -> ColorStyles.keyColor05N
    }
    val contentColor = when (status) {
        InspectionStatusUi.Pending -> ColorStyles.grey01
        InspectionStatusUi.Warning -> ColorStyles.error
        InspectionStatusUi.Done -> ColorStyles.keyColor01Dark02
    }

    Box(
        modifier = modifier
            .background(
                color = containerColor,
                shape = RoundedCornerShape(6.dp),
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(
            text = status.label,
            style = TextStyles.body03.medium,
            color = contentColor,
        )
    }
}

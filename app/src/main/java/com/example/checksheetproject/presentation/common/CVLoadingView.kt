package com.example.checksheetproject.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.checksheetproject.presentation.style.ColorStyles
import com.example.checksheetproject.presentation.style.TextStyles
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CVLoadingView(
    loading: StateFlow<Boolean>,
    modifier: Modifier = Modifier,
) {
    val isLoading by loading.collectAsStateWithLifecycle()

    CVLoadingView(
        isLoading = isLoading,
        modifier = modifier,
    )
}

@Composable
fun CVLoadingView(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isLoading) {
        CVLoadingDialog(modifier = modifier)
    }
}

@Composable
fun CVLoadingDialog(
    modifier: Modifier = Modifier,
    message: String = "처리 중입니다",
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(ColorStyles.dim),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = ColorStyles.white,
                tonalElevation = 6.dp,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                    )
                    Text(
                        text = message,
                        style = TextStyles.body02.regular,
                        color = ColorStyles.black,
                    )
                }
            }
        }
    }
}

package com.example.checksheetproject.presentation

import androidx.compose.runtime.Composable
import com.example.checksheetproject.presentation.navigation.CheckSheetNavGraph
import com.example.checksheetproject.presentation.theme.CheckSheetTheme

@Composable
fun CheckSheetApp() {
    CheckSheetTheme {
        CheckSheetNavGraph()
    }
}

package com.example.checksheetproject.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.checksheetproject.presentation.charger.ChargerListRoute
import com.example.checksheetproject.presentation.checklist.ChecklistRoute
import com.example.checksheetproject.presentation.cleaning.CleaningGuideRoute
import com.example.checksheetproject.presentation.inspectionitem.InspectionChargerEntryRoute
import com.example.checksheetproject.presentation.inspectionitem.InspectionItemListRoute
import com.example.checksheetproject.presentation.main.MainRoute

@Composable
fun CheckSheetNavGraph() {
    val backStack = remember { mutableStateListOf<AppRoute>(AppRoute.Main) }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = { route ->
            when (route) {
                AppRoute.Main -> NavEntry(route) {
                    MainRoute(
                        onFocusedChargersClick = {
                            backStack.add(AppRoute.ChargerList)
                        },
                        onInspectionItemsClick = {
                            backStack.add(AppRoute.InspectionChargerEntry)
                        },
                        onCleaningGuideClick = {
                            backStack.add(AppRoute.CleaningGuide)
                        },
                        onSettingsClick = {
                            backStack.add(AppRoute.Settings)
                        },
                    )
                }

                AppRoute.ChargerList -> NavEntry(route) {
                    ChargerListRoute(
                        onBackClick = { backStack.removeLastOrNull() },
                        onChargerClick = { chargerId ->
                            backStack.add(
                                AppRoute.Checklist(
                                    chargerId = chargerId,
                                    inspectionMonth = "2026-05",
                                ),
                            )
                        },
                    )
                }

                AppRoute.InspectionChargerEntry -> NavEntry(route) {
                    InspectionChargerEntryRoute(
                        onBackClick = { backStack.removeLastOrNull() },
                        onConfirmedClick = { chargerId ->
                            backStack.add(AppRoute.InspectionItemList(chargerId = chargerId))
                        },
                    )
                }

                is AppRoute.InspectionItemList -> NavEntry(route) {
                    InspectionItemListRoute(
                        chargerId = route.chargerId,
                        onBackClick = { backStack.removeLastOrNull() },
                        onSaveClick = {
                            backStack.clear()
                            backStack.add(AppRoute.Main)
                        },
                    )
                }

                AppRoute.CleaningGuide -> NavEntry(route) {
                    CleaningGuideRoute(
                        onBackClick = { backStack.removeLastOrNull() },
                    )
                }

                AppRoute.Settings -> NavEntry(route) {
                    MainPlaceholderScreen(
                        title = "설정",
                        description = "내부 APK 배포 정보와 앱 설정을 관리합니다.",
                        onBackClick = { backStack.removeLastOrNull() },
                    )
                }

                is AppRoute.Checklist -> NavEntry(route) {
                    ChecklistRoute(
                        chargerId = route.chargerId,
                        inspectionMonth = route.inspectionMonth,
                        onBackClick = { backStack.removeLastOrNull() },
                    )
                }
            }
        },
    )
}
